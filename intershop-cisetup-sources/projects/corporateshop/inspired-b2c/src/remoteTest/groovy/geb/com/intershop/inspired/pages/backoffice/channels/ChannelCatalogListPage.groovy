package geb.com.intershop.inspired.pages.backoffice.channels

import geb.com.intershop.inspired.pages.backoffice.catalog.CatalogListPage;

public class ChannelCatalogListPage extends CatalogListPage
{
    static at = {
        waitFor { $('table[data-testing-id="bo-catalog-list-channel"]').size() > 0 }
    }
    
    def catalogLink(catalogNameOrID)
    {
        $("a", href: contains("ViewChannelCatalog_52-Browse"), text: catalogNameOrID)
    }
}