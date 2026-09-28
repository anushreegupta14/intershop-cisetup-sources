package com.intershop.application.rest.custom.customer.internal.modules;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import com.intershop.application.rest.custom.customer.internal.CustomB2CCustomerROFunctionExtension;
import com.intershop.application.rest.custom.customer.internal.CustomSMBCustomerROFunctionExtension;
import com.intershop.component.customer.capi.CustomerBO;
import com.intershop.sellside.rest.b2c.capi.resourceobject.PrivateCustomerRO;
import com.intershop.sellside.rest.common.v1.capi.mapper.FunctionExtension;
import com.intershop.sellside.rest.smb.capi.resourceobject.SMBCustomerRO;

/**
 * Registers customer RO function extensions that add the ShowBackInStockDate
 * custom attribute to B2C and SMB/B2B customer REST responses.
 */
public class CustomCustomerMapperModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        Multibinder<FunctionExtension<CustomerBO, PrivateCustomerRO>> b2cExtensions =
            Multibinder.newSetBinder(binder(), new TypeLiteral<FunctionExtension<CustomerBO, PrivateCustomerRO>>() { });
        b2cExtensions.addBinding().to(CustomB2CCustomerROFunctionExtension.class);

        Multibinder<FunctionExtension<CustomerBO, SMBCustomerRO>> smbExtensions =
            Multibinder.newSetBinder(binder(), new TypeLiteral<FunctionExtension<CustomerBO, SMBCustomerRO>>() { });
        smbExtensions.addBinding().to(CustomSMBCustomerROFunctionExtension.class);
    }
}
