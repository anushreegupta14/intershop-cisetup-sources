package tests.com.intershop.application.rest.custom.customer.product.notification.capi;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.junit.Test;

import com.intershop.application.rest.custom.customer.product.notification.capi.CustomCustomerProductPriceNotificationResource;
import com.intershop.application.rest.custom.customer.product.notification.capi.CustomCustomerProductPriceNotificationsResource;
import com.intershop.application.rest.custom.customer.product.notification.capi.CustomNewProductNotificationRO;
import com.intershop.application.rest.custom.customer.product.notification.capi.CustomProductNotificationRO;
import com.intershop.component.product.capi.ProductBO;
import com.intershop.component.rest.capi.RestException;
import com.intershop.component.rest.capi.response.RestResponseBuilder;
import com.intershop.component.rest.capi.resourceobject.LinkRO;
import com.intershop.component.rest.capi.resourceobject.ResourceCollectionRO;
import com.intershop.component.user.capi.UserBO;
import com.intershop.sellside.appbase.b2c.capi.product.notification.ProductNotificationBO;
import com.intershop.sellside.rest.common.capi.resource.customer.product.notification.handler.CustomerProductNotificationsHandler;
import com.intershop.sellside.rest.common.capi.resource.customer.provider.CurrentCustomerProvider;

public class CustomCustomerProductPriceNotificationResourceTest
{
    @Test
    public void getProductPriceNotificationReturnsCustomROWhenFound()
    {
        UserBO userBO = mock(UserBO.class);
        CurrentCustomerProvider provider = mock(CurrentCustomerProvider.class);
        doReturn(userBO).when(provider).getCurrentUserBO();

        ProductNotificationBO productNotificationBO = mock(ProductNotificationBO.class);

        CustomerProductNotificationsHandler handler = mock(CustomerProductNotificationsHandler.class);
        doReturn(productNotificationBO).when(handler).getNotification(userBO, "123");

        CustomCustomerProductPriceNotificationResource resource = new CustomCustomerProductPriceNotificationResource();
        resource.setName("123");
        resource.setCurrentCustomerProvider(provider);
        resource.setHandler(handler);

        CustomProductNotificationRO result = resource.getProductPriceNotification();

        assertThat(result, notNullValue());
    }

    @Test(expected = RestException.class)
    public void getProductPriceNotificationThrowsNotFoundWhenMissing()
    {
        UserBO userBO = mock(UserBO.class);
        CurrentCustomerProvider provider = mock(CurrentCustomerProvider.class);
        doReturn(userBO).when(provider).getCurrentUserBO();

        CustomerProductNotificationsHandler handler = mock(CustomerProductNotificationsHandler.class);
        doReturn(null).when(handler).getNotification(userBO, "123");

        CustomCustomerProductPriceNotificationResource resource = new CustomCustomerProductPriceNotificationResource();
        resource.setName("123");
        resource.setCurrentCustomerProvider(provider);
        resource.setHandler(handler);

        resource.getProductPriceNotification();
    }

    @Test
    public void getNotificationsReturnsLinksForEachNotification()
    {
        UserBO userBO = mock(UserBO.class);
        CurrentCustomerProvider provider = mock(CurrentCustomerProvider.class);
        doReturn(userBO).when(provider).getCurrentUserBO();

        ProductBO productBO1 = mock(ProductBO.class);
        doReturn("Product 1").when(productBO1).getDisplayName();
        doReturn("sku1").when(productBO1).getSKU();

        ProductBO productBO2 = mock(ProductBO.class);
        doReturn("Product 2").when(productBO2).getDisplayName();
        doReturn("sku2").when(productBO2).getSKU();

        ProductNotificationBO notification1 = mock(ProductNotificationBO.class);
        doReturn(productBO1).when(notification1).getProductBO();

        ProductNotificationBO notification2 = mock(ProductNotificationBO.class);
        doReturn(productBO2).when(notification2).getProductBO();

        CustomerProductNotificationsHandler handler = mock(CustomerProductNotificationsHandler.class);
        doReturn(Arrays.asList(notification1, notification2)).when(handler).getNotifications(userBO);

        CustomCustomerProductPriceNotificationsResource resource = spy(new CustomCustomerProductPriceNotificationsResource());
        resource.setCurrentCustomerProvider(provider);
        resource.setHandler(handler);

        UriInfo uriInfo = mock(UriInfo.class);
        doReturn("/notifications").when(uriInfo).getPath();
        doReturn(uriInfo).when(resource).getUriInfo();

        ResourceCollectionRO<LinkRO> result = resource.getNotifications();

        assertThat(result, notNullValue());
        assertThat(result.getElements().size(), is(2));
    }

    @Test
    public void createProductNotificationReturnsCreatedResponse()
    {
        UserBO userBO = mock(UserBO.class);
        CurrentCustomerProvider provider = mock(CurrentCustomerProvider.class);
        doReturn(userBO).when(provider).getCurrentUserBO();

        ProductBO productBO = mock(ProductBO.class);
        doReturn("sku123").when(productBO).getSKU();

        ProductNotificationBO createdProductNotificationBO = mock(ProductNotificationBO.class);
        doReturn(productBO).when(createdProductNotificationBO).getProductBO();

        CustomerProductNotificationsHandler handler = mock(CustomerProductNotificationsHandler.class);
        doReturn(createdProductNotificationBO).when(handler).create(any(), any());

        UriInfo uriInfo = mock(UriInfo.class);
        doReturn("/notifications").when(uriInfo).getPath();

        RestResponseBuilder builder = mock(RestResponseBuilder.class);
        Response expectedResponse = mock(Response.class);
        when(builder.created(anyString())).thenReturn(builder);
        when(builder.build()).thenReturn(expectedResponse);

        CustomCustomerProductPriceNotificationsResource resource = spy(new CustomCustomerProductPriceNotificationsResource());
        resource.setCurrentCustomerProvider(provider);
        resource.setHandler(handler);

        doReturn(uriInfo).when(resource).getUriInfo();
        doReturn(builder).when(resource).getResponseBuilder();

        CustomNewProductNotificationRO newProductNotificationRO = new CustomNewProductNotificationRO();
        newProductNotificationRO.setSendEmail(true);

        Response actualResponse = resource.createProductNotification(newProductNotificationRO);

        assertThat(actualResponse, is(expectedResponse));
    }
}
