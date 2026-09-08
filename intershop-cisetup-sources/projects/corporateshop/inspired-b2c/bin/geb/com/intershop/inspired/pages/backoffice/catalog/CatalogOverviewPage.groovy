package geb.com.intershop.inspired.pages.backoffice.catalog

import geb.Page

class CatalogOverviewPage extends Page
{
    static at = {
        waitFor{ contentSlot.size()>0 }
    }
    
    static content= {
                
        contentSlot { $("div[data-testing-id='Catalog_Overview']") }
        linkProducts {contentSlot.$("a[data-testing-class='link-catalogs-products-channel']")}
    }
    
    def goToProducts()
    {
        linkProducts.click()
    }
}
