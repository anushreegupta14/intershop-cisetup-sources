package com.intershop.application.rest.custom.algolia.capi;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.MediaType;

import com.intershop.component.rest.capi.resource.AbstractRestResource;
import com.intershop.component.rest.capi.resource.RestResource;

public class AlgoliaTokenResource extends AbstractRestResource
{
    private AlgoliaTokenHandler handler;

    @Override
    public RestResource getRequestSpecificCopy(ResourceContext rc)
    {
        AlgoliaTokenResource copy = (AlgoliaTokenResource) super.getRequestSpecificCopy(rc);
        copy.setHandler(this.handler);
        return copy;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public AlgoliaTokenRO getToken()
    {
        return handler.getToken();
    }

    public void setHandler(AlgoliaTokenHandler handler)
    {
        this.handler = handler;
    }
}
