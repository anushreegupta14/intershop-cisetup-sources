package geb.com.intershop.inspired.pages.backoffice.catalog

import geb.com.intershop.inspired.pages.backoffice.catalog.CatalogCategoryDetailsSelectImagePage
import geb.com.intershop.inspired.pages.backoffice.catalog.CatalogCategoryDetailsGeneralTabOrganizationPage

class CatalogCategoryDetailsSelectImageOrganizationPage extends CatalogCategoryDetailsSelectImagePage
{
    static at = {
            waitFor{ $('table[data-testing-id^="bo-catalog-category-properties-image-selection-organization"]').size()>0 }
        }

    static content= {
        
        btnOkImageAssignment(to: [CatalogCategoryDetailsGeneralTabOrganizationPage, CatalogCategoryDetailsSelectImageOrganizationPage])    { imgAssignmentForm.$('input', name: 'BackFromImageSelection') }
        btnCancelImageAssignment(to: CatalogCategoryDetailsGeneralTabOrganizationPage) { imgAssignmentForm.$('input', name: 'CancelImageSelection') }

        btnSearchImage(to: CatalogCategoryDetailsSelectImageOrganizationPage)   { searchForm.$('input', name: 'searchImage') }
        
    }
}

