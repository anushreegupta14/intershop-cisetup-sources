package tests.com.intershop.application.rest.custom.customer.product.notification.capi;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

import org.junit.Test;

import com.intershop.application.rest.custom.customer.product.notification.capi.CustomNewProductNotificationRO;
import com.intershop.application.rest.custom.customer.product.notification.capi.CustomProductNotificationRO;
import com.intershop.beehive.core.capi.domain.AbstractPersistentObjectBO;
import com.intershop.beehive.core.capi.domain.Extensible;
import com.intershop.beehive.core.capi.domain.PersistentObject;
import com.intershop.sellside.appbase.b2c.capi.product.notification.ProductNotificationBO;

public class CustomProductNotificationTest
{
    @Test
    public void customProductNotificationROReadsSendEmailFromPersistentObject()
    {
        AbstractPersistentObjectBO<?> persistentBO = mock(AbstractPersistentObjectBO.class,
            withSettings().extraInterfaces(ProductNotificationBO.class));
        ProductNotificationBO productNotificationBO = (ProductNotificationBO) persistentBO;

        PersistentObject persistentObject = mock(PersistentObject.class,
            withSettings().extraInterfaces(Extensible.class));
        Extensible extensible = (Extensible) persistentObject;

        doReturn(persistentObject).when(persistentBO).getPersistentObject();
        doReturn(Boolean.TRUE).when(extensible).getBoolean("sendEmail");

        CustomProductNotificationRO notification = new CustomProductNotificationRO(productNotificationBO);

        assertThat(notification.isSendEmail(), is(true));
    }

    @Test
    public void customProductNotificationRODefaultsSendEmailForNonPersistentBO()
    {
        ProductNotificationBO productNotificationBO = mock(ProductNotificationBO.class);

        CustomProductNotificationRO notification = new CustomProductNotificationRO(productNotificationBO);

        assertThat(notification.isSendEmail(), is(false));
    }

    @Test
    public void customProductNotificationRODefaultsSendEmailForNonExtensibleObject()
    {
        AbstractPersistentObjectBO<?> persistentBO = mock(AbstractPersistentObjectBO.class,
            withSettings().extraInterfaces(ProductNotificationBO.class));
        ProductNotificationBO productNotificationBO = (ProductNotificationBO) persistentBO;
        PersistentObject persistentObject = mock(PersistentObject.class);

        doReturn(persistentObject).when(persistentBO).getPersistentObject();

        CustomProductNotificationRO notification = new CustomProductNotificationRO(productNotificationBO);

        assertThat(notification.isSendEmail(), is(false));
    }

    @Test
    public void customNewProductNotificationROStoresAndReturnsSendEmail()
    {
        CustomNewProductNotificationRO newNotification = new CustomNewProductNotificationRO();
        newNotification.setSendEmail(true);

        assertThat(newNotification.isSendEmail(), is(true));
    }

    @Test
    public void customNewProductNotificationRODefaultsSendEmailToFalse()
    {
        CustomNewProductNotificationRO newNotification = new CustomNewProductNotificationRO();

        assertThat(newNotification.isSendEmail(), is(false));
    }
}
