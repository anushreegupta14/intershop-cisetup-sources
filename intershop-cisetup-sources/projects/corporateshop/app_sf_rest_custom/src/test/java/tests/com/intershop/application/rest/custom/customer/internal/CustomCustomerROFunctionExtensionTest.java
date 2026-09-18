package tests.com.intershop.application.rest.custom.customer.internal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import com.intershop.application.rest.custom.customer.internal.CustomB2CCustomerROFunctionExtension;
import com.intershop.application.rest.custom.customer.internal.CustomSMBCustomerROFunctionExtension;
import com.intershop.beehive.businessobject.capi.BusinessObjectAttribute;
import com.intershop.beehive.businessobject.capi.BusinessObjectAttributes;
import com.intershop.component.customer.capi.CustomerBO;
import com.intershop.component.customer.capi.CustomerSegmentBO;
import com.intershop.component.customer.capi.CustomerSegmentBORepository;
import com.intershop.sellside.rest.b2c.capi.resourceobject.PrivateCustomerRO;
import com.intershop.sellside.rest.smb.capi.resourceobject.SMBCustomerRO;

public class CustomCustomerROFunctionExtensionTest
{
    @Test
    public void addsNewAttributesAndOnlyAssignedSegmentsForBothVariants()
    {
        CustomerBO customer = mock(CustomerBO.class);
        BusinessObjectAttributes attributes = mock(BusinessObjectAttributes.class);
        BusinessObjectAttribute<Object> currency = mock(BusinessObjectAttribute.class);
        BusinessObjectAttribute<Object> hideInvoice = mock(BusinessObjectAttribute.class);
        when(customer.getExtension(BusinessObjectAttributes.class)).thenReturn(attributes);
        when(attributes.getAttribute("DefaultCurrency")).thenReturn(currency);
        when(attributes.getAttribute("HideInvoiceDocument")).thenReturn(hideInvoice);
        when(currency.getValue()).thenReturn("USD");
        when(hideInvoice.getValue()).thenReturn(Boolean.FALSE);
        CustomerSegmentBORepository repository = mock(CustomerSegmentBORepository.class);
        CustomerSegmentBO assigned = mock(CustomerSegmentBO.class);
        CustomerSegmentBO unassigned = mock(CustomerSegmentBO.class);
        when(assigned.getID()).thenReturn("ReturningCustomers");
        when(assigned.isCustomerBOAssigned(customer)).thenReturn(true);
        when(repository.getAllCustomerSegmentBOs()).thenReturn(Arrays.asList(unassigned, assigned));
        PrivateCustomerRO b2c = mock(PrivateCustomerRO.class);
        SMBCustomerRO smb = mock(SMBCustomerRO.class);
        new CustomB2CCustomerROFunctionExtension() {
            @Override protected CustomerSegmentBORepository getSegmentRepository() { return repository; }
        }.apply(customer, b2c);
        new CustomSMBCustomerROFunctionExtension() {
            @Override protected CustomerSegmentBORepository getSegmentRepository() { return repository; }
        }.apply(customer, smb);
        for (com.intershop.sellside.rest.common.capi.resourceobject.customer.CustomerRO response : Arrays.asList(b2c, smb))
        {
            verify(response).addCustomField("DefaultCurrency", "USD");
            verify(response).addCustomField("HideInvoiceDocument", Boolean.FALSE);
            verify(response).addCustomField("customerSegments", Collections.singletonList("ReturningCustomers"));
        }
    }

    @Test
    public void returnsEmptySegmentsWhenNoneAreAssigned()
    {
        CustomerSegmentBORepository repository = mock(CustomerSegmentBORepository.class);
        when(repository.getAllCustomerSegmentBOs()).thenReturn(Collections.emptyList());
        PrivateCustomerRO response = mock(PrivateCustomerRO.class);
        new CustomB2CCustomerROFunctionExtension() {
            @Override protected CustomerSegmentBORepository getSegmentRepository() { return repository; }
        }.apply(mock(CustomerBO.class), response);
        verify(response).addCustomField("customerSegments", Collections.emptyList());
    }

        @Test
    public void omitsMissingAttribute()
    {
        PrivateCustomerRO response = mock(PrivateCustomerRO.class);
        new CustomB2CCustomerROFunctionExtension().apply(mock(CustomerBO.class), response);
        verify(response, never()).addCustomField(anyString(), any());
    }
}
