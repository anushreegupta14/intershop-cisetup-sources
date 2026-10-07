package tests.com.intershop.application.rest.custom.user.internal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.intershop.application.rest.custom.user.internal.CustomSMBCustomerUserROFunctionExtension;
import com.intershop.component.user.capi.UserBO;
import com.intershop.sellside.rest.smb.capi.resourceobject.SMBCustomerUserRO;

public class CustomSMBCustomerUserROFunctionExtensionTest
{
    @Test
    public void addsUserUUIDAndFormattedUUID()
    {
        UserBO user = mock(UserBO.class);
        SMBCustomerUserRO target = mock(SMBCustomerUserRO.class);

        when(user.getID()).thenReturn("abc-123_def@456");

        new CustomSMBCustomerUserROFunctionExtension().apply(user, target);

        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Object> valueCaptor = ArgumentCaptor.forClass(Object.class);
        verify(target, times(2)).addCustomField(keyCaptor.capture(), valueCaptor.capture());

        Map<String, Object> captured = new HashMap<>();
        for (int i = 0; i < keyCaptor.getAllValues().size(); i++)
        {
            captured.put(keyCaptor.getAllValues().get(i), valueCaptor.getAllValues().get(i));
        }

        assertThat(captured, hasEntry("userUUID", "abc-123_def@456"));
        assertThat(captured, hasEntry("formattedUUID", "abc-123_def456"));
    }
}
