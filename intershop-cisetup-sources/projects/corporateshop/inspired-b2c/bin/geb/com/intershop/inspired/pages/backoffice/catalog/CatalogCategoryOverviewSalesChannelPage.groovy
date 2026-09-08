package geb.com.intershop.inspired.pages.backoffice.catalog

import geb.com.intershop.inspired.pages.backoffice.catalog.CatalogCategoryDetailsGeneralTabChannelPage
import geb.com.intershop.inspired.pages.backoffice.catalog.CatalogCategoryOverviewPage

class CatalogCategoryOverviewSalesChannelPage extends CatalogCategoryOverviewPage
{
    static content= {
        tableAldiBox         { $('table[data-testing-id^="bo-catalog-category-overview-channel"]') }
        
        btnEditProperties(to: CatalogCategoryDetailsGeneralTabChannelPage)   { editCategoryForm.$('input', name: 'edit') }
    }
    private def catalogLink(catalogNameOrID)
    {
        categoryForm.$("a", href: contains("ViewChannelCatalog_52-Browse"), text: catalogNameOrID)
    }
}

