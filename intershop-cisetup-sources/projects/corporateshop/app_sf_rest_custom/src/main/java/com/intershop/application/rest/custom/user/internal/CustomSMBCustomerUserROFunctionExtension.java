package com.intershop.application.rest.custom.user.internal;

import java.util.regex.Pattern;

import com.intershop.component.user.capi.UserBO;
import com.intershop.sellside.rest.common.v1.capi.mapper.FunctionExtension;
import com.intershop.sellside.rest.smb.capi.resourceobject.SMBCustomerUserRO;

public class CustomSMBCustomerUserROFunctionExtension implements FunctionExtension<UserBO, SMBCustomerUserRO>
{
    private static final Pattern DISALLOWED = Pattern.compile("[^A-Za-z0-9_-]");
    private static final int MAX_LENGTH = 64;

    @Override
    public boolean isApplicable(UserBO source, SMBCustomerUserRO target)
    {
        return source != null && target != null;
    }

    @Override
    public SMBCustomerUserRO apply(UserBO source, SMBCustomerUserRO target)
    {
        if (source == null || target == null)
        {
            return target;
        }

        String userUUID = source.getID();
        if (userUUID != null && !userUUID.isEmpty())
        {
            target.addCustomField("userUUID", userUUID);

            String formatted = DISALLOWED.matcher(userUUID).replaceAll("");
            if (formatted.length() > MAX_LENGTH)
            {
                formatted = formatted.substring(0, MAX_LENGTH);
            }
            target.addCustomField("formattedUUID", formatted);
        }

        return target;
    }
}
