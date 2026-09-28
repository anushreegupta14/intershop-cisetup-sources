package com.intershop.application.rest.custom.customer.product.notification.capi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.intershop.beehive.core.capi.domain.AbstractPersistentObjectBO;
import com.intershop.beehive.core.capi.domain.Extensible;
import com.intershop.beehive.core.capi.domain.PersistentObject;
import com.intershop.sellside.appbase.b2c.capi.product.notification.ProductNotificationBO;
import com.intershop.sellside.rest.common.capi.resourceobject.product.notification.ProductNotificationRO;

public class CustomProductNotificationRO extends ProductNotificationRO
{
    private boolean sendEmail = false;

    public CustomProductNotificationRO()
    {
        super();
    }

    public CustomProductNotificationRO(ProductNotificationBO productNotificationBO)
    {
        super(productNotificationBO);
        this.sendEmail = readSendEmail(productNotificationBO);
    }

    private static boolean readSendEmail(ProductNotificationBO productNotificationBO)
    {
        if (!(productNotificationBO instanceof AbstractPersistentObjectBO))
        {
            return false;
        }

        PersistentObject persistentObject = ((AbstractPersistentObjectBO<?>) productNotificationBO).getPersistentObject();
        if (!(persistentObject instanceof Extensible))
        {
            return false;
        }

        Boolean value = ((Extensible) persistentObject).getBoolean("sendEmail");
        return value != null && value.booleanValue();
    }

    @JsonProperty("sendEmail")
    @JsonInclude(JsonInclude.Include.ALWAYS)
    public boolean isSendEmail()
    {
        return sendEmail;
    }

    public void setSendEmail(boolean sendEmail)
    {
        this.sendEmail = sendEmail;
    }
}
