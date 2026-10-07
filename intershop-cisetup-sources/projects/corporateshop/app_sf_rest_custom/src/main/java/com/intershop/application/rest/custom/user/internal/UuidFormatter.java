package com.intershop.application.rest.custom.user.internal;

/**
 * Sanitizes a raw user token to the format required by Algolia Analytics:
 * only A-Z, a-z, 0-9, '-' and '_' are allowed; the result is capped at 64 characters.
 */
public final class UuidFormatter
{
    private static final int MAX_LENGTH = 64;
    private static final String DISALLOWED = "[^A-Za-z0-9_-]";

    private UuidFormatter() {}

    /**
     * @param raw the raw token, e.g. a user BO ID or BasicProfile ID
     * @return a sanitized token, or {@code null} if the input is {@code null}
     */
    public static String format(String raw)
    {
        if (raw == null)
        {
            return null;
        }
        String sanitized = raw.replaceAll(DISALLOWED, "");
        return sanitized.length() > MAX_LENGTH ? sanitized.substring(0, MAX_LENGTH) : sanitized;
    }
}
