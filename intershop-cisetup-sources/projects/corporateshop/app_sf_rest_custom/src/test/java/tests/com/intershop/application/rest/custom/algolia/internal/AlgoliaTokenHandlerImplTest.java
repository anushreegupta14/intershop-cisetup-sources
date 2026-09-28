package tests.com.intershop.application.rest.custom.algolia.internal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.intershop.application.rest.custom.algolia.capi.AlgoliaTokenRO;
import com.intershop.application.rest.custom.algolia.internal.AlgoliaTokenHandlerImpl;
import com.intershop.beehive.app.capi.AppContext;
import com.intershop.beehive.core.capi.domain.Domain;
import com.intershop.beehive.core.capi.domain.DomainMgr;
import com.intershop.component.application.capi.ApplicationBO;
import com.intershop.component.catalog.capi.CatalogBO;
import com.intershop.component.catalog.capi.CatalogBORepository;
import com.intershop.component.catalog.capi.CatalogBORepositoryExtension;
import com.intershop.component.repository.capi.BusinessObjectRepositoryContext;
import com.intershop.component.rest.capi.RestException;

public class AlgoliaTokenHandlerImplTest
{
    @Test
    public void getTokenGeneratesTokenForOnlineCatalog()
    {
        TestableAlgoliaTokenHandlerImpl handler = new TestableAlgoliaTokenHandlerImpl();

        AppContext appContext = mock(AppContext.class);
        ApplicationBO application = mock(ApplicationBO.class);
        Domain site = mock(Domain.class);
        Domain currentDomain = mock(Domain.class);
        DomainMgr domainMgr = mock(DomainMgr.class);
        BusinessObjectRepositoryContext repositoryContext = mock(BusinessObjectRepositoryContext.class);
        CatalogBORepository catalogRepository = mock(CatalogBORepository.class);
        CatalogBO onlineCatalog = mock(CatalogBO.class);
        Domain catalogDomain = mock(Domain.class);

        doReturn("inspired.b2c").when(site).getDomainName();
        doReturn(site).when(application).getSite();
        doReturn(application).when(appContext).getVariable("CurrentApplicationBO");
        doReturn(repositoryContext).when(appContext).getVariable(BusinessObjectRepositoryContext.CURRENT);
        doReturn(catalogRepository).when(repositoryContext).getRepository(CatalogBORepositoryExtension.EXTENSION_ID);
        doReturn(Arrays.asList(onlineCatalog)).when(catalogRepository).getAllCatalogBOs();
        doReturn(true).when(onlineCatalog).isOnline();
        doReturn(catalogDomain).when(onlineCatalog).getCatalogDomain();
        doReturn("inspired.b2c").when(catalogDomain).getDomainName();
        doReturn("inspired").when(onlineCatalog).getName();
        doReturn(currentDomain).when(domainMgr).getDomainByName("inspiredb2c");
        doReturn("inspired.b2c").when(currentDomain).getDomainName();

        handler.setAppContext(appContext);
        handler.setDomainMgr(domainMgr);
        handler.setProperty("algoliasearchID", "secret");
        handler.setProperty("algoliaapplicationID", "app-id");

        AlgoliaTokenRO token = handler.getToken();

        assertThat(token, notNullValue());
        assertThat(token.getToken(), notNullValue());
        assertThat(token.getToken().isEmpty(), is(false));
        assertThat(token.getExpiryTime(), notNullValue());
    }

    @Test
    public void getTokenGeneratesTokenWithEmptyFilterWhenNoOnlineCatalogs()
    {
        TestableAlgoliaTokenHandlerImpl handler = new TestableAlgoliaTokenHandlerImpl();

        AppContext appContext = mock(AppContext.class);
        ApplicationBO application = mock(ApplicationBO.class);
        Domain site = mock(Domain.class);
        Domain currentDomain = mock(Domain.class);
        DomainMgr domainMgr = mock(DomainMgr.class);
        BusinessObjectRepositoryContext repositoryContext = mock(BusinessObjectRepositoryContext.class);
        CatalogBORepository catalogRepository = mock(CatalogBORepository.class);

        doReturn("inspired.b2c").when(site).getDomainName();
        doReturn(site).when(application).getSite();
        doReturn(application).when(appContext).getVariable("CurrentApplicationBO");
        doReturn(repositoryContext).when(appContext).getVariable(BusinessObjectRepositoryContext.CURRENT);
        doReturn(catalogRepository).when(repositoryContext).getRepository(CatalogBORepositoryExtension.EXTENSION_ID);
        doReturn(Collections.emptyList()).when(catalogRepository).getAllCatalogBOs();
        doReturn(currentDomain).when(domainMgr).getDomainByName("inspiredb2c");
        doReturn("inspired.b2c").when(currentDomain).getDomainName();

        handler.setAppContext(appContext);
        handler.setDomainMgr(domainMgr);

        AlgoliaTokenRO token = handler.getToken();

        assertThat(token, notNullValue());
        assertThat(token.getToken(), notNullValue());
    }

    @Test(expected = RestException.class)
    public void getTokenThrowsWhenCurrentApplicationBOMissing()
    {
        TestableAlgoliaTokenHandlerImpl handler = new TestableAlgoliaTokenHandlerImpl();

        AppContext appContext = mock(AppContext.class);
        doReturn(null).when(appContext).getVariable("CurrentApplicationBO");

        handler.setAppContext(appContext);

        handler.getToken();
    }

    private static class TestableAlgoliaTokenHandlerImpl extends AlgoliaTokenHandlerImpl
    {
        private AppContext appContext;
        private DomainMgr domainMgr;
        private final Map<String, String> properties = new HashMap<>();

        public void setAppContext(AppContext appContext)
        {
            this.appContext = appContext;
        }

        public void setDomainMgr(DomainMgr domainMgr)
        {
            this.domainMgr = domainMgr;
        }

        public void setProperty(String name, String value)
        {
            this.properties.put(name, value);
        }

        @Override
        protected AppContext getCurrentAppContext()
        {
            return appContext;
        }

        @Override
        protected DomainMgr getDomainMgr()
        {
            return domainMgr;
        }

        @Override
        protected String getProperty(String name)
        {
            return properties.get(name);
        }
    }
}
