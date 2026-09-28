package com.intershop.application.rest.custom.customer.product.notification.internal;

import com.intershop.application.rest.custom.customer.product.notification.capi.CustomNewProductNotificationRO;
import com.intershop.beehive.core.capi.domain.AbstractPersistentObjectBO;
import com.intershop.beehive.core.capi.domain.ExtensibleObjectPO;
import com.intershop.beehive.core.capi.domain.PersistentObject;
import com.intershop.sellside.appbase.b2c.capi.product.notification.ProductNotificationBO;
import com.intershop.sellside.appbase.b2c.capi.product.notification.ProductNotificationBORepository;
import com.intershop.sellside.rest.common.capi.resource.customer.product.notification.operator.CustomerProductNotificationOperator;
import com.intershop.sellside.rest.common.capi.resourceobject.product.notification.NewProductNotificationRO;
import com.intershop.sellside.rest.common.internal.resource.customer.product.notification.operator.CustomerPriceThresholdProductNotificationOperatorImpl;

public class CustomCustomerProductPriceNotificationOperatorImpl
    extends CustomerPriceThresholdProductNotificationOperatorImpl
    implements CustomerProductNotificationOperator
{
    private static final String SEND_EMAIL_ATTRIBUTE = "sendEmail";

    @Override
    public ProductNotificationBO create(NewProductNotificationRO newProductNotificationRO,
                                        ProductNotificationBORepository repository)
    {
        ProductNotificationBO productNotificationBO = super.create(newProductNotificationRO, repository);

        boolean sendEmail = false;
        if (newProductNotificationRO instanceof CustomNewProductNotificationRO)
        {
            sendEmail = ((CustomNewProductNotificationRO) newProductNotificationRO).isSendEmail();
        }

        setSendEmail(productNotificationBO, sendEmail);
        return productNotificationBO;
    }

    private void setSendEmail(ProductNotificationBO productNotificationBO, boolean sendEmail)
    {
        if (!(productNotificationBO instanceof AbstractPersistentObjectBO))
        {
            return;
        }

        PersistentObject persistentObject = ((AbstractPersistentObjectBO<?>) productNotificationBO).getPersistentObject();
        if (persistentObject instanceof ExtensibleObjectPO)
        {
            ((ExtensibleObjectPO) persistentObject).putBoolean(SEND_EMAIL_ATTRIBUTE, Boolean.valueOf(sendEmail));
        }
    }
}
