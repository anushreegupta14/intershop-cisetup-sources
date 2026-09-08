package geb.com.intershop.inspired.pages.backoffice.catalog

import geb.com.intershop.inspired.pages.backoffice.catalog.CatalogCategoryDetailsGeneralTabPage
import geb.com.intershop.inspired.pages.backoffice.catalog.CatalogCategoryDetailsSelectImageChannelPage

class CatalogCategoryDetailsGeneralTabChannelPage extends CatalogCategoryDetailsGeneralTabPage
{
    static at =
    {
       waitFor{ $('table[data-testing-id^="page-bo-category-edit-general-tab-channel"]').size()>0 }
    }
    
    static content= {
    
        btnCatImageSelect(to: CatalogCategoryDetailsSelectImageChannelPage)    { $('input', name: 'browseDirectoryAdd') }
        catImagePathInput           { $("input", name: "RegFormAddCategory_Image") }

    }

    def currentlyAssignedCategoryImage()
    {
        waitFor{ ($(catImagePathInput).size() > 0) }
        return categoryForm.$("input", name: "RegFormAddCategory_Image").value()
    }

}
