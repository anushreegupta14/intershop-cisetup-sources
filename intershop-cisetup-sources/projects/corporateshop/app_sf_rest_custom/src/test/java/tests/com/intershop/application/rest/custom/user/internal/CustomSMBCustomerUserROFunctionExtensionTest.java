package tests.com.intershop.application.rest.custom.user.internal;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.intershop.application.rest.custom.user.internal.CustomSMBCustomerUserROFunctionExtension;
import com.intershop.component.user.capi.UserBO;
import com.intershop.sellside.rest.smb.capi.resourceobject.SMBCustomerUserRO;

public class CustomSMBCustomerUserROFunctionExtensionTest
{
    @Test
    public void addsPreferredLanguageEvenWhenUuidIsMissing()
    {
        UserBO user = mock(UserBO.class);
        com.intershop.component.user.capi.UserBOPreferencesExtension preferences =
            mock(com.intershop.component.user.capi.UserBOPreferencesExtension.class);
        com.intershop.beehive.core.capi.localization.LocaleInformation locale =
            mock(com.intershop.beehive.core.capi.localization.LocaleInformation.class);
        when(user.getExtension(com.intershop.component.user.capi.UserBOPreferencesExtension.class)).thenReturn(preferences);
        when(preferences.getPreferredLanguage()).thenReturn(locale);
        when(locale.getLocaleID()).thenReturn("en_US");
        SMBCustomerUserRO response = mock(SMBCustomerUserRO.class);

        new CustomSMBCustomerUserROFunctionExtension().apply(user, response);

    }

    @Test
    public void addsProfileUuidWithoutPersistenceExtension()
    {
        UserBO user = mock(UserBO.class);
        SMBCustomerUserRO response = mock(SMBCustomerUserRO.class);
        when(user.getID()).thenReturn("abc.DEF_123-xyz");

        assertSame(response, new CustomSMBCustomerUserROFunctionExtension().apply(user, response));

        verify(response).addCustomField("useruuid", "abc.DEF_123-xyz");
        verify(response).addCustomField("formattedUUID", "abcDEF123xyz");
    }
}
