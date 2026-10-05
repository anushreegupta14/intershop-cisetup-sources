package com.intershop.application.rest.custom.algolia.capi;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AlgoliaTokenRO
{
    private String token;
    private String expiryTime;

    public AlgoliaTokenRO()
    {
    }

    public AlgoliaTokenRO(String token, String expiryTime)
    {
        this.token = token;
        this.expiryTime = expiryTime;
    }

    @JsonProperty("token")
    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    @JsonProperty("expiryTime")
    public String getExpiryTime()
    {
        return expiryTime;
    }

    public void setExpiryTime(String expiryTime)
    {
        this.expiryTime = expiryTime;
    }
}
