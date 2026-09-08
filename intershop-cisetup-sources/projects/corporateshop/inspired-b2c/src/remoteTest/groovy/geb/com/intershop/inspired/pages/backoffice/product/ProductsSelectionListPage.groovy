package geb.com.intershop.inspired.pages.backoffice.product

import geb.com.intershop.inspired.pages.backoffice.BackOfficePage
import geb.com.intershop.inspired.pages.backoffice.modules.ProductsSelectionListEntry

class ProductsSelectionListPage extends BackOfficePage
{
    static at =
    {
       waitFor{ contentSlot.size()>0 }
    }

    static content= {
        
        contentSlot             { $('table[data-testing-id="bo-product-selection-list-page"]') }

        searchForm              { $("form", name:"detailForm") }
        
        searchInput             { searchForm.$('input', id: 'WFSimpleSearch_NameOrID') }
        
        btnFind                 { searchForm.$('input', name: 'findSimple') }
        btnOK(to: ProductDetailsLinksPage)                   { searchForm.$('input', name: 'assign') }
        btnCancel               { searchForm.$('input', name: 'cancel') }

        productsSelectionList    { $('.products_selection_list_row').collect{ it.module(ProductsSelectionListEntry) } }
    }


    //------------------------------------------------------------
    // link functions
    //------------------------------------------------------------
    def clickFind()
    {
        btnFind.click()
    }

    def clickOK()
    {
        btnOK.click()
    }

    def clickCancel()
    {
        btnCancel.click()
    }

    def searchProduct(productNameOrID)
    {
        searchInput.value  productNameOrID
        btnFind.click();
        sleepForNSeconds(5); // TODO: Find a way to use waitFor to determine when page is reloaded(add info box, etc.)
        waitFor{searchForm.size()>0}
        waitFor{$("a", text: contains(productNameOrID)).size()>0}
    }

    def selectProductBySKU(productSKU)
    {
        def product = productsSelectionList.findAll().find{
            
            it.sku == productSKU
            
        }
        
        if( product != null )
        {
            product.selected = true
        }
        else
            println "not found"
       
    }   

    def findAndAcceptProduct(sku)
    {
        searchProduct sku
        selectProductBySKU sku
        clickOK()
    }

}

