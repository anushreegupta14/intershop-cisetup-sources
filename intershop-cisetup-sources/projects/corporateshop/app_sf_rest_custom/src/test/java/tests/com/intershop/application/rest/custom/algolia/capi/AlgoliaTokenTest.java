package tests.com.intershop.application.rest.custom.algolia.capi;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.intershop.application.rest.custom.algolia.capi.AlgoliaTokenHandler;
import com.intershop.application.rest.custom.algolia.capi.AlgoliaTokenResource;
import com.intershop.application.rest.custom.algolia.capi.AlgoliaTokenRO;

public class AlgoliaTokenTest
{
    @Test
    public void algoliaTokenROStoresTokenAndExpiryTime()
    {
        AlgoliaTokenRO token = new AlgoliaTokenRO("test-token", "2026-09-28T12:00:00Z");

        assertThat(token.getToken(), is("test-token"));
        assertThat(token.getExpiryTime(), is("2026-09-28T12:00:00Z"));
    }

    @Test
    public void algoliaTokenROSetterAndGettersWork()
    {
        AlgoliaTokenRO token = new AlgoliaTokenRO();
        token.setToken("another-token");
        token.setExpiryTime("2026-10-01T00:00:00Z");

        assertThat(token.getToken(), is("another-token"));
        assertThat(token.getExpiryTime(), is("2026-10-01T00:00:00Z"));
    }

    @Test
    public void algoliaTokenResourceReturnsTokenFromHandler()
    {
        AlgoliaTokenHandler handler = mock(AlgoliaTokenHandler.class);
        AlgoliaTokenRO expected = new AlgoliaTokenRO("token", "2026-09-28T12:00:00Z");
        when(handler.getToken()).thenReturn(expected);

        AlgoliaTokenResource resource = new AlgoliaTokenResource();
        resource.setHandler(handler);

        AlgoliaTokenRO actual = resource.getToken();

        assertThat(actual, is(expected));
    }
}
