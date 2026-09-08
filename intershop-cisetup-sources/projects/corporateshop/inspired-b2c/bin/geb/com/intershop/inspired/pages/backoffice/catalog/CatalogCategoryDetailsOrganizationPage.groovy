package geb.com.intershop.inspired.pages.backoffice.catalog

import geb.com.intershop.inspired.pages.backoffice.catalog.CatalogCategoryDetailsPage
import geb.com.intershop.inspired.pages.backoffice.catalog.CatalogCategoryDetailsGeneralTabOrganizationPage
import geb.com.intershop.inspired.pages.backoffice.catalog.CatalogCategoryOverviewOrganizationPage


class CatalogCategoryDetailsOrganizationPage extends CatalogCategoryDetailsPage
{
    static at = {
            waitFor{ $('table[data-testing-id^="page-bo-catalog-details-organization"]').size()>0 }
        }

    static content= {
        linkGeneral(to: CatalogCategoryDetailsGeneralTabOrganizationPage)  { $("a",href:contains(~/ViewCategory-Edit/)) }
        linkContent                 { $("a",href:contains(~/ViewCategory-ListPageletEntryPoints/)) }
        linkSubCategories           { $("a",href:contains(~/ViewCategorySubCategory-ListAll/)) }
        linkAttributes              { $("a",href:contains(~/ViewCategory-ManageCA/)) }
        linkLabels                  { $("a",href:contains(~/ViewObjectLabels-GetLabelsByObject/)) }
        linkLinks                   { $("a",href:contains(~/ViewCategoryLinks-Start/)) }
        
        btnBackToList(to: CatalogCategoryOverviewOrganizationPage)   { backToListForm.$('input', name: 'back') }
      
    }

}
