package geb.com.intershop.inspired.pages.backoffice.catalog

import geb.com.intershop.inspired.pages.backoffice.catalog.CatalogCategoryDetailsGeneralTabOrganizationPage
import geb.com.intershop.inspired.pages.backoffice.catalog.CatalogCategoryOverviewPage

class CatalogCategoryOverviewOrganizationPage extends CatalogCategoryOverviewPage
{
    static content= {
        tableAldiBox         { $('table[data-testing-id^="bo-catalog-category-overview-organization"]') }

        btnEditProperties(to: CatalogCategoryDetailsGeneralTabOrganizationPage)   { editCategoryForm.$('input', name: 'edit') }
    }
    private def catalogLink(catalogNameOrID)
    {
        categoryForm.$("a", href: contains("ViewBrowseCatalogCategory-Start"), text: catalogNameOrID)
    }
}

