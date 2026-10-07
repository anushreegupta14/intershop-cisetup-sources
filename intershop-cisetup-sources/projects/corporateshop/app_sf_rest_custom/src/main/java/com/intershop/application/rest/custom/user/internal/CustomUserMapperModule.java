package com.intershop.application.rest.custom.user.internal;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import com.intershop.component.user.capi.UserBO;
import com.intershop.sellside.rest.common.v1.capi.mapper.FunctionExtension;
import com.intershop.sellside.rest.smb.capi.resourceobject.SMBCustomerUserRO;

/**
 * Registers the custom SMB customer user RO function extension with the Guice object graph.
 */
public class CustomUserMapperModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        Multibinder<FunctionExtension<UserBO, SMBCustomerUserRO>> binder =
            Multibinder.newSetBinder(binder(),
                new TypeLiteral<FunctionExtension<UserBO, SMBCustomerUserRO>>() { });
        binder.addBinding().to(CustomSMBCustomerUserROFunctionExtension.class);
    }
}
