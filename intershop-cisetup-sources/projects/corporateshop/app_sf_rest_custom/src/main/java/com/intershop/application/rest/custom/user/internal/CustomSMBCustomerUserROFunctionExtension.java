package com.intershop.application.rest.custom.user.internal;

import com.intershop.beehive.core.capi.localization.LocaleInformation;
import com.intershop.component.user.capi.UserBO;
import com.intershop.component.user.capi.UserBOPreferencesExtension;
import com.intershop.sellside.rest.common.v1.capi.mapper.FunctionExtension;
import com.intershop.sellside.rest.smb.capi.resourceobject.SMBCustomerUserRO;

/**
 * Adds the user's basic profile UUID and an alphanumeric-only formatted UUID
 * to the SMB/B2B customer user REST response.
 */
public class CustomSMBCustomerUserROFunctionExtension implements FunctionExtension<UserBO, SMBCustomerUserRO>
{
    private static final String USER_UUID = "useruuid";
    private static final String FORMATTED_UUID = "formattedUUID";

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

        // The platform's BasicProfilePOToUserBOMapper uses the profile UUID as the BO ID.
        String uuid = source.getID();
        if (uuid != null)
        {
            target.addCustomField(USER_UUID, uuid);
            target.addCustomField(FORMATTED_UUID, uuid.replaceAll("[^A-Za-z0-9]", ""));
        }

        UserBOPreferencesExtension preferences = source.getExtension(UserBOPreferencesExtension.class);
        LocaleInformation language = preferences == null ? null : preferences.getPreferredLanguage();
        if (language != null)
        {
            target.addCustomField("PreferredLanguage", language.getLocaleID());
        }

        return target;
    }
}
