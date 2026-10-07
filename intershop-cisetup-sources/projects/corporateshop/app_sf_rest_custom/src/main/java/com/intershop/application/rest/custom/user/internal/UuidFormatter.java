package com.intershop.application.rest.custom.user.internal;

import java.util.regex.Pattern;

/**
 * Formats a user UUID so that it only contains the characters A-Z, a-z, 0-9, '-' and '_'
 * and is at most {@value #MAX_LENGTH} characters long. All other characters are removed
 * before the result is truncated.
 */
public final class UuidFormatter
{
    public static final int MAX_LENGTH = 64;

    private static final Pattern NOT_ALLOWED = Pattern.compile("[^A-Za-z0-9_-]");

    private UuidFormatter()
    {
    }

    public static String format(String value)
    {
        if (value == null)
        {
            return null;
        }

        String formatted = NOT_ALLOWED.matcher(value).replaceAll("");
        return formatted.length() > MAX_LENGTH ? formatted.substring(0, MAX_LENGTH) : formatted;
    }
}
