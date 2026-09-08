package geb.com.intershop.inspired.pages.backoffice.catalog

import geb.com.intershop.inspired.pages.backoffice.catalog.CatalogCategoryDetailsPage
import geb.com.intershop.inspired.pages.backoffice.catalog.CatalogCategoryDetailsGeneralTabChannelPage
import geb.com.intershop.inspired.pages.backoffice.catalog.CatalogCategoryOverviewSalesChannelPage


class CatalogCategoryDetailsChannelPage extends CatalogCategoryDetailsPage
{
    static at = {
            waitFor{ $('table[data-testing-id^="page-bo-catalog-details-channel"]').size()>0 }
        }

    static content= {
        linkGeneral(to: CatalogCategoryDetailsGeneralTabChannelPage)   { $("a",href:contains(~/ViewChannelCategory.*-Edit/)) }
        linkContent                 { $("a",href:contains(~/ViewChannelCatalogCategoryContent.*-ListPageletEntryPoints/)) }
        linkSubCategories           { $("a",href:contains(~/ViewChannelCategorySubCategory.*-ListAll/)) }
        linkAttributes              { $("a",href:contains(~/ViewChannelCategory.*-ManageCA/)) }
        linkLabels                  { $("a",href:contains(~/ViewChannelObjectLabels-GetLabelsByObject/)) }
        linkLinks                   { $("a",href:contains(~/ViewCategoryLinks-Start/)) }
        
        btnBackToList(to: CatalogCategoryOverviewSalesChannelPage)   { backToListForm.$('input', name: 'back') }
      
    }

}

