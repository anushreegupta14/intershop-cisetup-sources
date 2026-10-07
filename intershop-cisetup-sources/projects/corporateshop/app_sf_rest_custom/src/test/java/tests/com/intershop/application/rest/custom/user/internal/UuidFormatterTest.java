package tests.com.intershop.application.rest.custom.user.internal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import org.junit.Test;

import com.intershop.application.rest.custom.user.internal.UuidFormatter;

public class UuidFormatterTest
{
    private static final String ALLOWED_PATTERN = "^[A-Za-z0-9_-]{0,64}$";

    @Test
    public void returnsNullForNullInput()
    {
        assertThat(UuidFormatter.format(null), is(nullValue()));
    }

    @Test
    public void returnsEmptyForEmptyInput()
    {
        assertThat(UuidFormatter.format(""), is(""));
    }

    @Test
    public void retainsStandardUuidWithHyphens()
    {
        assertThat(UuidFormatter.format("550e8400-e29b-41d4-a716-446655440000"),
                   is("550e8400-e29b-41d4-a716-446655440000"));
    }

    @Test
    public void removesDotFromIcmUuid()
    {
        assertThat(UuidFormatter.format("oCUKAB2tbUgAAAFn.xMNA0gK"), is("oCUKAB2tbUgAAAFnxMNA0gK"));
    }

    @Test
    public void removesSpecialCharactersAndWhitespace()
    {
        assertThat(UuidFormatter.format("user+name@domain.com"), is("usernamedomaincom"));
        assertThat(UuidFormatter.format("a.b*c/d?e"), is("abcde"));
        assertThat(UuidFormatter.format("name|value"), is("namevalue"));
        assertThat(UuidFormatter.format("a b\tc\nd"), is("abcd"));
    }

    @Test
    public void retainsUnderscoresAndHyphens()
    {
        assertThat(UuidFormatter.format("user_name-123"), is("user_name-123"));
    }

    @Test
    public void removesNonAsciiCharacters()
    {
        assertThat(UuidFormatter.format("\u00e4b\u00e9c\u20acd\u00dfe"), is("bcde"));
    }

    @Test
    public void keepsExactlyMaxLength()
    {
        String input = repeat("a", UuidFormatter.MAX_LENGTH);
        assertThat(UuidFormatter.format(input), is(input));
    }

    @Test
    public void truncatesToMaxLength()
    {
        assertThat(UuidFormatter.format(repeat("b", 65)), is(repeat("b", 64)));
        assertThat(UuidFormatter.format(repeat("a", 100)), is(repeat("a", 64)));
    }

    @Test
    public void removesSpecialCharactersBeforeTruncating()
    {
        StringBuilder input = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        for (int i = 0; i < 66; i++)
        {
            char c = (char)('a' + (i % 26));
            input.append(c);
            expected.append(c);
            if (i < 10)
            {
                input.append('.');
            }
        }
        assertThat(UuidFormatter.format(input.toString()), is(expected.substring(0, 64)));
    }

    @Test
    public void returnsEmptyWhenOnlySpecialCharacters()
    {
        assertThat(UuidFormatter.format("!@#$%^&*()"), is(""));
    }

    @Test
    public void resultAlwaysMatchesAllowedPattern()
    {
        String[] inputs = { "oCUKAB2tbUgAAAFn.xMNA0gK", "user+name@domain.com", repeat("x.y", 50),
                            "\u00e4\u00f6\u00fc", "ABC-def_123", " " };
        for (String input : inputs)
        {
            assertThat(input, UuidFormatter.format(input).matches(ALLOWED_PATTERN), is(true));
        }
    }

    @Test
    public void isNonInstantiableUtilityClass() throws Exception
    {
        Constructor<UuidFormatter> constructor = UuidFormatter.class.getDeclaredConstructor();
        assertThat(Modifier.isFinal(UuidFormatter.class.getModifiers()), is(true));
        assertThat(Modifier.isPrivate(constructor.getModifiers()), is(true));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    private static String repeat(String s, int count)
    {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++)
        {
            builder.append(s);
        }
        return builder.toString();
    }
}
