package geb.com.intershop.inspired.pages.backoffice.channels.organization

import geb.com.intershop.inspired.pages.backoffice.catalog.CatalogListPage

public class OrgCatalogListPage extends CatalogListPage
{
    static at = {
        waitFor { $('table[data-testing-id="bo-catalog-list-organization"]').size() > 0 }
    }
    
    def catalogLink(catalogNameOrID)
    {
        $("a",href: contains("ViewBrowseCatalogCategory-Start"), text: catalogNameOrID)
    }
}