package com.intershop.application.rest.custom.user.internal;

import com.intershop.component.user.capi.UserBO;
import com.intershop.sellside.rest.common.v1.capi.mapper.FunctionExtension;
import com.intershop.sellside.rest.smb.capi.resourceobject.SMBCustomerUserRO;

/**
 * Adds {@code userUUID} and {@code formattedUUID} to the SMB customer user REST response.
 * The raw value is read from the user BO ID; the formatted value is safe for Algolia Analytics.
 */
public class CustomSMBCustomerUserROFunctionExtension implements FunctionExtension<UserBO, SMBCustomerUserRO>
{
    @Override
    public boolean isApplicable(UserBO source, SMBCustomerUserRO target)
    {
        return source != null && source.getID() != null;
    }

    @Override
    public SMBCustomerUserRO apply(UserBO source, SMBCustomerUserRO target)
    {
        String userUUID = source.getID();
        target.addCustomField("userUUID", userUUID);
        target.addCustomField("formattedUUID", UuidFormatter.format(userUUID));
        return target;
    }
}
