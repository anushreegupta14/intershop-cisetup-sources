package com.intershop.application.rest.custom.customer.product.notification.capi;

import java.util.Collection;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.intershop.component.product.capi.ProductBO;
import com.intershop.component.rest.capi.RestUtils;
import com.intershop.component.rest.capi.resourceobject.LinkRO;
import com.intershop.component.rest.capi.resourceobject.ResourceCollectionRO;
import com.intershop.component.user.capi.UserBO;
import com.intershop.sellside.appbase.b2c.capi.product.notification.ProductNotificationBO;
import com.intershop.sellside.rest.common.capi.resource.customer.AbstractCustomerRestCollectionResource;
import com.intershop.sellside.rest.common.capi.resource.customer.product.notification.handler.CustomerProductNotificationsHandler;

public class CustomCustomerProductPriceNotificationsResource
    extends AbstractCustomerRestCollectionResource<CustomerProductNotificationsHandler>
{
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
    public ResourceCollectionRO<LinkRO> getNotifications()
    {
        ResourceCollectionRO<LinkRO> productNotifications = new ResourceCollectionRO<>();
        UserBO userBO = getCurrentCustomerProvider().getCurrentUserBO();
        Collection<ProductNotificationBO> notifications = getHandler().getNotifications(userBO);

        for (ProductNotificationBO notification : notifications)
        {
            ProductBO product = notification.getProductBO();
            String displayName = product.getDisplayName();
            String sku = product.getSKU();
            String path = RestUtils.addToURI(getUriInfo().getPath(), sku);
            productNotifications.addElement(new LinkRO(displayName, path));
        }

        return productNotifications;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
    public Response createProductNotification(CustomNewProductNotificationRO newProductNotificationRO)
    {
        UserBO userBO = getCurrentCustomerProvider().getCurrentUserBO();
        CustomerProductNotificationsHandler handler = getHandler();

        handler.validateCreation(newProductNotificationRO, userBO);
        ProductNotificationBO createdProductNotificationBO = handler.create(newProductNotificationRO, userBO);

        String path = RestUtils.addToURI(
            getUriInfo().getPath(),
            createdProductNotificationBO.getProductBO().getSKU());

        return getResponseBuilder().created(path).build();
    }
}
