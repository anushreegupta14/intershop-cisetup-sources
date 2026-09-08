package geb.com.intershop.inspired.pages.backoffice.catalog

import geb.com.intershop.inspired.pages.backoffice.catalog.CatalogCategoryDetailsSelectImagePage
import geb.com.intershop.inspired.pages.backoffice.catalog.CatalogCategoryDetailsGeneralTabChannelPage

class CatalogCategoryDetailsSelectImageChannelPage extends CatalogCategoryDetailsSelectImagePage
{
    static at = {
            waitFor{ $('table[data-testing-id^="bo-catalog-category-properties-image-selection-channel"]').size()>0 }
        }

    static content= {
        
        btnOkImageAssignment(to: [CatalogCategoryDetailsGeneralTabChannelPage, CatalogCategoryDetailsSelectImageChannelPage])    { imgAssignmentForm.$('input', name: 'BackFromImageSelection') }
        btnCancelImageAssignment(to: CatalogCategoryDetailsGeneralTabChannelPage)  { imgAssignmentForm.$('input', name: 'CancelImageSelection') }

        btnSearchImage(to: CatalogCategoryDetailsSelectImageChannelPage)        { searchForm.$('input', name: 'searchImage') }
        
    }
}

