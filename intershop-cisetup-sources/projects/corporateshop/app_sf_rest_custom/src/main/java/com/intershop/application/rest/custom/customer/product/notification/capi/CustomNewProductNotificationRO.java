package com.intershop.application.rest.custom.customer.product.notification.capi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.intershop.sellside.rest.common.capi.resourceobject.product.notification.NewProductNotificationRO;

public class CustomNewProductNotificationRO extends NewProductNotificationRO
{
    private boolean sendEmail = false;

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
