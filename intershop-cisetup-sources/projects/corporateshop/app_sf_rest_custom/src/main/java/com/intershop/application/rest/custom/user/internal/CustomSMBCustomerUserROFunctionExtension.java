package com.intershop.application.rest.custom.user.internal;

import com.intershop.component.user.capi.UserBO;
import com.intershop.sellside.rest.common.v1.capi.mapper.FunctionExtension;
import com.intershop.sellside.rest.smb.capi.resourceobject.SMBCustomerUserRO;

/**
 * Adds the user UUID and an Algolia-compatible formatted variant of it to the B2B customer user REST response.
 * Existing response fields are left untouched.
 */
public class CustomSMBCustomerUserROFunctionExtension implements FunctionExtension<UserBO, SMBCustomerUserRO>
{
    public static final String USER_UUID = "userUUID";
    public static final String FORMATTED_UUID = "formattedUUID";

    @Override
    public boolean isApplicable(UserBO source, SMBCustomerUserRO target)
    {
        return source != null && source.getID() != null && !source.getID().isEmpty();
    }

    @Override
    public SMBCustomerUserRO apply(UserBO source, SMBCustomerUserRO target)
    {
        String userUUID = source.getID();
        target.addCustomField(USER_UUID, userUUID);
        target.addCustomField(FORMATTED_UUID, UuidFormatter.format(userUUID));
        return target;
    }
}
