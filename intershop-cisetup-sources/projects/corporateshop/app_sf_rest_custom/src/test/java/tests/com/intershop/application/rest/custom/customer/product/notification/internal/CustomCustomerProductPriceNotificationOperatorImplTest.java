package tests.com.intershop.application.rest.custom.customer.product.notification.internal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.withSettings;

import org.junit.Test;

import com.intershop.application.rest.custom.customer.product.notification.capi.CustomNewProductNotificationRO;
import com.intershop.application.rest.custom.customer.product.notification.internal.CustomCustomerProductPriceNotificationOperatorImpl;
import com.intershop.beehive.core.capi.domain.AbstractPersistentObjectBO;
import com.intershop.beehive.core.capi.domain.ExtensibleObjectPO;
import com.intershop.sellside.appbase.b2c.capi.product.notification.ProductNotificationBO;
import com.intershop.sellside.appbase.b2c.capi.product.notification.ProductNotificationBORepository;
import com.intershop.sellside.rest.common.capi.resourceobject.product.notification.NewProductNotificationRO;

public class CustomCustomerProductPriceNotificationOperatorImplTest
{
    @Test
    public void createSetsSendEmailAttributeToTrueForCustomRO()
    {
        TestableCustomCustomerProductPriceNotificationOperatorImpl operator = new TestableCustomCustomerProductPriceNotificationOperatorImpl();
        ExtensibleObjectPO persistentObject = mock(ExtensibleObjectPO.class);
        ProductNotificationBO productNotificationBO = createPersistentProductNotificationBO(persistentObject);
        operator.setParentReturn(productNotificationBO);

        CustomNewProductNotificationRO customRO = new CustomNewProductNotificationRO();
        customRO.setSendEmail(true);

        ProductNotificationBO created = operator.create(customRO, mock(ProductNotificationBORepository.class));

        assertThat(created, is(productNotificationBO));
        verify(persistentObject).putBoolean("sendEmail", Boolean.TRUE);
    }

    @Test
    public void createSetsSendEmailAttributeToFalseForNonCustomRO()
    {
        TestableCustomCustomerProductPriceNotificationOperatorImpl operator = new TestableCustomCustomerProductPriceNotificationOperatorImpl();
        ExtensibleObjectPO persistentObject = mock(ExtensibleObjectPO.class);
        ProductNotificationBO productNotificationBO = createPersistentProductNotificationBO(persistentObject);
        operator.setParentReturn(productNotificationBO);

        NewProductNotificationRO ro = mock(NewProductNotificationRO.class);

        ProductNotificationBO created = operator.create(ro, mock(ProductNotificationBORepository.class));

        assertThat(created, is(productNotificationBO));
        verify(persistentObject).putBoolean("sendEmail", Boolean.FALSE);
    }

    @Test
    public void createDoesNotFailForNonPersistentBO()
    {
        TestableCustomCustomerProductPriceNotificationOperatorImpl operator = new TestableCustomCustomerProductPriceNotificationOperatorImpl();
        ProductNotificationBO productNotificationBO = mock(ProductNotificationBO.class);
        operator.setParentReturn(productNotificationBO);

        NewProductNotificationRO ro = mock(NewProductNotificationRO.class);

        ProductNotificationBO created = operator.create(ro, mock(ProductNotificationBORepository.class));

        assertThat(created, is(productNotificationBO));
    }

    private static ProductNotificationBO createPersistentProductNotificationBO(ExtensibleObjectPO persistentObject)
    {
        AbstractPersistentObjectBO<?> persistentBO = mock(AbstractPersistentObjectBO.class,
            withSettings().extraInterfaces(ProductNotificationBO.class));
        doReturn(persistentObject).when(persistentBO).getPersistentObject();
        return (ProductNotificationBO) persistentBO;
    }

    private static class TestableCustomCustomerProductPriceNotificationOperatorImpl
        extends CustomCustomerProductPriceNotificationOperatorImpl
    {
        private ProductNotificationBO parentReturn;

        public void setParentReturn(ProductNotificationBO parentReturn)
        {
            this.parentReturn = parentReturn;
        }

        @Override
        protected ProductNotificationBO createParent(NewProductNotificationRO newProductNotificationRO,
                                                     ProductNotificationBORepository repository)
        {
            return parentReturn;
        }
    }
}
