package com.intershop.application.rest.custom.user.internal.modules;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import com.intershop.application.rest.custom.user.internal.CustomSMBCustomerUserROFunctionExtension;
import com.intershop.component.user.capi.UserBO;
import com.intershop.sellside.rest.common.v1.capi.mapper.FunctionExtension;
import com.intershop.sellside.rest.smb.capi.resourceobject.SMBCustomerUserRO;

/**
 * Contributes the custom user extension to the platform's SMB customer user RO mapper extensions.
 */
public class CustomUserMapperModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        Multibinder<FunctionExtension<UserBO, SMBCustomerUserRO>> extensions = Multibinder.newSetBinder(binder(),
                        new TypeLiteral<FunctionExtension<UserBO, SMBCustomerUserRO>>() { });
        extensions.addBinding().to(CustomSMBCustomerUserROFunctionExtension.class);
    }
}
