package tests.com.intershop.application.rest.custom.user.internal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.intershop.application.rest.custom.user.internal.CustomSMBCustomerUserROFunctionExtension;
import com.intershop.component.user.capi.UserBO;
import com.intershop.sellside.rest.smb.capi.resourceobject.SMBCustomerUserRO;

public class CustomSMBCustomerUserROFunctionExtensionTest
{
    private static final String ICM_UUID = "oCUKAB2tbUgAAAFn.xMNA0gK";
    private static final String FORMATTED_ICM_UUID = "oCUKAB2tbUgAAAFnxMNA0gK";

    private CustomSMBCustomerUserROFunctionExtension extension;
    private SMBCustomerUserRO target;

    @Before
    public void setUp()
    {
        extension = new CustomSMBCustomerUserROFunctionExtension();
        target = new SMBCustomerUserRO();
    }

    @Test
    public void isNotApplicableForNullUser()
    {
        assertThat(extension.isApplicable(null, target), is(false));
    }

    @Test
    public void isNotApplicableWhenUserHasNoId()
    {
        assertThat(extension.isApplicable(user(null), target), is(false));
    }

    @Test
    public void isNotApplicableWhenUserHasEmptyId()
    {
        assertThat(extension.isApplicable(user(""), target), is(false));
    }

    @Test
    public void isApplicableWhenUserHasId()
    {
        assertThat(extension.isApplicable(user(ICM_UUID), target), is(true));
    }

    @Test
    public void addsUserUuidAndFormattedUuid()
    {
        extension.apply(user(ICM_UUID), target);

        Map<String, Object> customFields = target.getCustomFields();
        assertThat(customFields, hasEntry("userUUID", (Object)ICM_UUID));
        assertThat(customFields, hasEntry("formattedUUID", (Object)FORMATTED_ICM_UUID));
    }

    @Test
    public void returnsSameTargetInstance()
    {
        assertThat(extension.apply(user(ICM_UUID), target), is(sameInstance(target)));
    }

    @Test
    public void keepsExistingFieldsUntouched()
    {
        target.setEmail("jlink@test.intershop.de");
        target.setPhoneHome("+49 3641 50 1000");
        target.addCustomField("existingField", "existingValue");

        extension.apply(user(ICM_UUID), target);

        assertThat(target.getEmail(), is("jlink@test.intershop.de"));
        assertThat(target.getPhoneHome(), is("+49 3641 50 1000"));
        assertThat(target.getCustomFields(), hasEntry("existingField", (Object)"existingValue"));
    }

    @Test
    public void addsExactlyTheTwoCaseSensitiveFieldNames()
    {
        extension.apply(user(ICM_UUID), target);

        assertThat(target.getCustomFields().keySet(),
                   is(new HashSet<>(Arrays.asList("userUUID", "formattedUUID"))));
        assertThat(CustomSMBCustomerUserROFunctionExtension.USER_UUID, is("userUUID"));
        assertThat(CustomSMBCustomerUserROFunctionExtension.FORMATTED_UUID, is("formattedUUID"));
    }

    @Test
    public void keepsRawUserUuidUnmodified()
    {
        String rawId = "user+name@domain.com";

        extension.apply(user(rawId), target);

        assertThat(target.getCustomFields(), hasEntry("userUUID", (Object)rawId));
        assertThat(target.getCustomFields(), hasEntry("formattedUUID", (Object)"usernamedomaincom"));
    }

    @Test
    public void readsOnlyTheBusinessObjectId()
    {
        UserBO user = user(ICM_UUID);

        extension.apply(user, target);

        verify(user, atLeastOnce()).getID();
        verifyNoMoreInteractions(user);
    }

    private static UserBO user(String id)
    {
        UserBO user = mock(UserBO.class);
        when(user.getID()).thenReturn(id);
        return user;
    }
}
