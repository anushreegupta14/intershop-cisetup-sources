package tests.com.intershop.application.rest.custom.product.internal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import com.intershop.application.rest.custom.product.internal.CustomProductSearchHandlerImpl;
import com.intershop.beehive.core.capi.domain.Domain;
import com.intershop.beehive.core.capi.domain.ExtensibleObject;
import com.intershop.beehive.core.capi.domain.PersistentObjectBOExtension;
import com.intershop.beehive.core.capi.naming.NamingMgr;
import com.intershop.beehive.core.capi.paging.PageableIterator;
import com.intershop.beehive.core.capi.paging.PagingMgr;
import com.intershop.beehive.core.capi.pipeline.PipelineDictionary;
import com.intershop.beehive.core.capi.request.Request;
import com.intershop.beehive.core.request.test.RequestRule;
import com.intershop.component.product.capi.ProductBO;
import com.intershop.sellside.rest.common.capi.resourceobject.ProductRO;

public class CustomProductSearchHandlerImplTest
{
    @Rule
    public RequestRule requestRule = new RequestRule();

    @BeforeClass
    public static void initNamingManager()
    {
        NamingMgr namingMgr = mock(NamingMgr.class);
        PagingMgr pagingMgr = mock(PagingMgr.class);
        PageableIterator pageableIterator = mock(PageableIterator.class);
        when(namingMgr.lookupManager("PagingMgr")).thenReturn(pagingMgr);
        when(pagingMgr.createPageable(any(java.util.Iterator.class), anyInt())).thenReturn(pageableIterator);
        when(pageableIterator.getID()).thenReturn("test-pageable");
        NamingMgr.setInstance(namingMgr);
    }

    private CustomProductSearchHandlerImpl handler;
    private ProductBO product;
    private ExtensibleObject storedProduct;
    private ProductRO.ProductROBuilder builder;

    @Before
    public void setUp()
    {
        product = mock(ProductBO.class);
        storedProduct = mock(ExtensibleObject.class);
        PersistentObjectBOExtension extension = mock(PersistentObjectBOExtension.class);
        when(product.getExtension(PersistentObjectBOExtension.class)).thenReturn(extension);
        when(extension.getPersistentObject()).thenReturn(storedProduct);
        when(storedProduct.createAttributeNamesIterator()).thenReturn(Collections.emptyIterator());
        builder = new ProductRO.ProductROBuilder(Collections.emptyList());
        handler = Mockito.spy(new CustomProductSearchHandlerImpl());
        // No attributes are registered in the standard ProductDetail group.
        doReturn(Collections.emptyIterator()).when(handler).getProductDetailAttributes(any(Domain.class));
        Request request = requestRule.create();
        PipelineDictionary dictionary = mock(PipelineDictionary.class);
        when(request.getPipelineDictionary()).thenReturn(dictionary);
        when(dictionary.get("CurrentChannel")).thenReturn(mock(Domain.class));
    }

    @Test
    public void returnsUngroupedAttributesAndPreservesValueTypes()
    {
        when(storedProduct.createAttributeNamesIterator()).thenReturn(
            Arrays.asList("ungrouped", "flag", "count", "values", "unset").iterator());
        when(product.getAttribute("ungrouped")).thenReturn("saved value");
        when(product.getAttribute("flag")).thenReturn(Boolean.FALSE);
        when(product.getAttribute("count")).thenReturn(0);
        when(product.getAttribute("values")).thenReturn(Arrays.asList("a", "b"));
        builder.getAttributes().put("existing", "retained");

        handler.setCustomAttributes(builder, product);

        assertThat(builder.getAttributes(), hasEntry("ungrouped", "saved value"));
        assertThat(builder.getAttributes(), hasEntry("flag", Boolean.FALSE));
        assertThat(builder.getAttributes(), hasEntry("count", 0));
        assertThat(builder.getAttributes(), hasEntry("values", Arrays.asList("a", "b")));
        assertThat(builder.getAttributes(), hasEntry("existing", "retained"));
        assertThat(builder.getAttributes().containsKey("unset"), is(false));
    }

    @Test
    public void persistedEcofriendlyOverridesDefault()
    {
        when(storedProduct.createAttributeNamesIterator()).thenReturn(
            Collections.singletonList("Ecofriendly").iterator());
        when(product.getAttribute("Ecofriendly")).thenReturn(Boolean.FALSE);
        handler.setCustomAttributes(builder, product);
        assertThat(builder.getAttributes(), hasEntry("Ecofriendly", Boolean.FALSE));
    }

    @Test
    public void preservesEcofriendlyDefaultWithoutSavedValue()
    {
        handler.setCustomAttributes(builder, product);
        assertThat(builder.getAttributes(), hasEntry("Ecofriendly", Boolean.TRUE));
    }

    @Test
    public void handlesMissingPersistenceExtension()
    {
        when(product.getExtension(PersistentObjectBOExtension.class)).thenReturn(null);
        handler.setCustomAttributes(builder, product);
        assertThat(builder.getAttributes(), hasEntry("Ecofriendly", Boolean.TRUE));
    }

    @Test
    public void handlesNullProduct()
    {
        handler.setCustomAttributes(builder, null);
        assertThat(builder.getAttributes(), hasEntry("Ecofriendly", Boolean.TRUE));
    }
}
