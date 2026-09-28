package com.intershop.application.rest.custom.customer.product.notification.capi;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.intershop.component.rest.capi.RestException;
import com.intershop.component.user.capi.UserBO;
import com.intershop.sellside.appbase.b2c.capi.product.notification.ProductNotificationBO;
import com.intershop.sellside.rest.common.capi.resource.customer.product.notification.CustomerProductPriceNotificationResource;

public class CustomCustomerProductPriceNotificationResource extends CustomerProductPriceNotificationResource
{
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
    @Override
    public CustomProductNotificationRO getProductPriceNotification()
    {
        UserBO userBO = getCurrentCustomerProvider().getCurrentUserBO();
        ProductNotificationBO productNotificationBO = getHandler().getNotification(userBO, getName());

        if (productNotificationBO == null)
        {
            throw new RestException().notFound();
        }

        return new CustomProductNotificationRO(productNotificationBO);
    }
}
