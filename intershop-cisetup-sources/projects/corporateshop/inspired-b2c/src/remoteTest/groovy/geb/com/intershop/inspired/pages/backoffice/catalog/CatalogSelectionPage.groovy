package geb.com.intershop.inspired.pages.backoffice.catalog

import geb.com.intershop.inspired.pages.backoffice.BackOfficePage
import geb.com.intershop.inspired.pages.backoffice.product.ProductDetailsLinksPage

class CatalogSelectionPage extends BackOfficePage
{
    static at =
            {
                waitFor{ contentSlot.size()>0 }
            }

    static content = {
        contentSlot             { $('table[data-testing-id="bo-catalog-selection-page"]') }

        catalogSelectionForm { $("form", name:"CatalogSelectionForm") }
        btnOK(to: ProductDetailsLinksPage)                   { catalogSelectionForm.$('input', name: 'finish') }
        btnCancel               { catalogSelectionForm.$('input', name: 'cancel') }
        catalogs {$('.dynatree-checkbox')}
    }

    def clickOKBtn()
    {
        btnOK.click()
    }
}
