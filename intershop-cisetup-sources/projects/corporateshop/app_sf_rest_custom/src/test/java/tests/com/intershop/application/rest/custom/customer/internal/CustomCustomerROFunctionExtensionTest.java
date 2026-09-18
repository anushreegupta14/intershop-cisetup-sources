package tests.com.intershop.application.rest.custom.customer.internal;

import static org.mockito.Mockito.*;

import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;

import com.intershop.application.rest.custom.customer.internal.CustomB2CCustomerROFunctionExtension;
import com.intershop.beehive.businessobject.capi.BusinessObjectAttribute;
import com.intershop.beehive.businessobject.capi.BusinessObjectAttributes;
import com.intershop.beehive.core.capi.domain.ExtensibleObject;
import com.intershop.component.customer.capi.CustomerBO;
import com.intershop.sellside.rest.b2c.capi.resourceobject.PrivateCustomerRO;
import com.intershop.sellside.rest.smb.capi.resourceobject.SMBCustomerRO;

public class CustomCustomerROFunctionExtensionTest
{
    @Test
    public void addsNewAttributesAndOnlyAssignedSegmentsForBothVariants()
    {
        CustomerBO customer = mock(CustomerBO.class);
        BusinessObjectAttributes attributes = mock(BusinessObjectAttributes.class);
        BusinessObjectAttribute<Object> hideInvoice = mock(BusinessObjectAttribute.class);
        when(customer.getExtension(BusinessObjectAttributes.class)).thenReturn(attributes);       
        when(attributes.getAttribute("HideInvoiceDocument")).thenReturn(hideInvoice);
        when(hideInvoice.getValue()).thenReturn(Boolean.FALSE);     
        for (com.intershop.sellside.rest.common.capi.resourceobject.customer.CustomerRO response : Arrays.asList(b2c, smb))
        {
            verify(response).addCustomField("HideInvoiceDocument", Boolean.FALSE);
        }
    }

    @Test
    public void omitsMissingAttribute()
    {
        PrivateCustomerRO response = mock(PrivateCustomerRO.class);
        new CustomB2CCustomerROFunctionExtension().apply(mock(CustomerBO.class), response);
        verify(response, never()).addCustomField(anyString(), any());
    }
}
