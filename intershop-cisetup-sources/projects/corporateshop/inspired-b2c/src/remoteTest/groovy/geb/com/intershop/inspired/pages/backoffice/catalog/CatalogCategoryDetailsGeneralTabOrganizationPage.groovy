package geb.com.intershop.inspired.pages.backoffice.catalog

import geb.com.intershop.inspired.pages.backoffice.catalog.CatalogCategoryDetailsGeneralTabPage
import geb.com.intershop.inspired.pages.backoffice.catalog.CatalogCategoryDetailsSelectImageOrganizationPage

class CatalogCategoryDetailsGeneralTabOrganizationPage extends CatalogCategoryDetailsGeneralTabPage
{
    static at =
    {
       waitFor{ $('table[data-testing-id^="page-bo-category-edit-general-tab-organization"]').size()>0 }
    }
    
    static content= {
    
        btnCatImageSelect(to: CatalogCategoryDetailsSelectImageOrganizationPage)    { $('input', name: 'browseDirectoryAdd') }
        catImagePathInput           { $("input", name: "RegFormEditCatalog_Image") }

    }

    def currentlyAssignedCategoryImage()
    {
        waitFor{ ($(catImagePathInput).size() > 0) }
        return categoryForm.$("input", name: "RegFormEditCatalog_Image").value()
    }

}
