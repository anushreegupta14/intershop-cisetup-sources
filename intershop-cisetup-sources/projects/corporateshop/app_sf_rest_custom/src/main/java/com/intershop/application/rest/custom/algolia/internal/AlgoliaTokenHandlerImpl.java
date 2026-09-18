package com.intershop.application.rest.custom.algolia.internal;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Collection;
import java.util.StringJoiner;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;

import com.intershop.application.rest.custom.algolia.capi.AlgoliaTokenHandler;
import com.intershop.application.rest.custom.algolia.capi.AlgoliaTokenRO;
import com.intershop.beehive.app.capi.AppContext;
import com.intershop.beehive.core.capi.app.AppContextUtil;
import com.intershop.component.application.capi.ApplicationBO;
import com.intershop.beehive.core.capi.domain.Domain;
import com.intershop.beehive.core.capi.domain.DomainMgr;
import com.intershop.beehive.core.capi.environment.PropertyMgr;
import com.intershop.beehive.core.capi.naming.NamingMgr;
import com.intershop.component.catalog.capi.CatalogBO;
import com.intershop.component.catalog.capi.CatalogBORepository;
import com.intershop.component.catalog.capi.CatalogBORepositoryExtension;
import com.intershop.component.repository.capi.BusinessObjectRepositoryContext;
import com.intershop.component.rest.capi.RestException;

public class AlgoliaTokenHandlerImpl implements AlgoliaTokenHandler
{
    private static final String REGEX = "[^A-Za-z0-9\\-_]";

    @Override
    public AlgoliaTokenRO getToken()
    {
        AppContext appContext = AppContextUtil.getCurrentAppContext();
        if (appContext == null)
        {
            throw new RestException(500, "No current application context");
        }

        ApplicationBO currentApplication = (ApplicationBO) appContext.getVariable("CurrentApplicationBO");
        if (currentApplication == null)
        {
            throw new RestException(500, "No current application");
        }

        Domain site = currentApplication.getSite();
        if (site == null)
        {
            throw new RestException(500, "No current site");
        }

        String rawDomainName = site.getDomainName();
        if (rawDomainName == null)
        {
            throw new RestException(500, "Current site has no domain name");
        }

        String cleanedDomainName = rawDomainName.replaceAll(REGEX, "");

        DomainMgr domainMgr = NamingMgr.getInstance().get(DomainMgr.class);
        if (domainMgr == null)
        {
            throw new RestException(500, "Domain manager not available");
        }

        Domain currentDomain = domainMgr.getDomainByName(cleanedDomainName);
        if (currentDomain == null)
        {
            throw new RestException(500, "No domain found for " + cleanedDomainName);
        }

        BusinessObjectRepositoryContext repositoryContext =
            (BusinessObjectRepositoryContext) appContext.getVariable(BusinessObjectRepositoryContext.CURRENT);
        if (repositoryContext == null)
        {
            throw new RestException(500, "No business object repository context");
        }

        CatalogBORepository catalogRepository =
            repositoryContext.getRepository(CatalogBORepositoryExtension.EXTENSION_ID);
        if (catalogRepository == null)
        {
            throw new RestException(500, "No catalog repository available");
        }

        StringJoiner filterJoiner = new StringJoiner(" OR ");
        Collection<? extends CatalogBO> catalogs = catalogRepository.getAllCatalogBOs();
        for (CatalogBO catalog : catalogs)
        {
            if (catalog == null || !catalog.isOnline())
            {
                continue;
            }

            Domain catalogDomain = catalog.getCatalogDomain();
            if (catalogDomain == null)
            {
                continue;
            }

            if (currentDomain.getDomainName().equals(catalogDomain.getDomainName()))
            {
                filterJoiner.add("AssignedCatalogID:" + catalog.getName());
            }
        }

        String filter = filterJoiner.toString();
        String parentApiKey = PropertyMgr.getInstance().getProperty("algoliasearchID");
        if (parentApiKey == null || parentApiKey.isEmpty())
        {
            parentApiKey = "dummy-algolia-search-key";
        }
        String applicationId = PropertyMgr.getInstance().getProperty("algoliaapplicationID");
        if (applicationId == null || applicationId.isEmpty())
        {
            applicationId = "dummy-algolia-application-id";
        }

        Instant expiry = Instant.now().plusSeconds(86400L);
        long validUntil = expiry.getEpochSecond();

        String token;
        try
        {
            String encodedFilters = URLEncoder.encode(filter, "UTF-8");
            String query = "filters=" + encodedFilters + "&validUntil=" + validUntil;

            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(parentApiKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] hmac = mac.doFinal(query.getBytes(StandardCharsets.UTF_8));

            StringBuilder hex = new StringBuilder();
            for (byte b : hmac)
            {
                hex.append(String.format("%02x", b & 0xFF));
            }

            token = Base64.getEncoder().encodeToString((hex.toString() + query).getBytes(StandardCharsets.UTF_8));
        }
        catch (Exception e)
        {
            throw new RestException(500, "Failed to generate Algolia token");
        }

        return new AlgoliaTokenRO(token, expiry.toString());
    }
}
