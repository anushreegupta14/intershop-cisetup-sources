package com.intershop.application.rest.custom.user.internal.modules;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import com.intershop.application.rest.custom.user.internal.CustomSMBCustomerUserROFunctionExtension;
import com.intershop.component.user.capi.UserBO;
import com.intershop.sellside.rest.common.v1.capi.mapper.FunctionExtension;
import com.intershop.sellside.rest.smb.capi.resourceobject.SMBCustomerUserRO;

/**
 * Registers a {@link FunctionExtension} that adds UUID fields to the SMB/B2B
 * customer user REST response. It contributes to the same Multibinder that the
 * platform {@code AppSfRestSMBMapperModule} sets up.
 */
public class CustomUserMapperModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        Multibinder<FunctionExtension<UserBO, SMBCustomerUserRO>> multibinder =
            Multibinder.newSetBinder(binder(), new TypeLiteral<FunctionExtension<UserBO, SMBCustomerUserRO>>() { });
        multibinder.addBinding().to(CustomSMBCustomerUserROFunctionExtension.class);
    }
}
