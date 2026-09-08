package geb.com.intershop.inspired.pages.backoffice.product

import geb.com.intershop.inspired.pages.backoffice.BackOfficePage
import geb.com.intershop.inspired.pages.backoffice.modules.ProductListEntry

class ProductListOrganizationPage extends ProductListPage 
{
    static content= {
        contentSlot         { $('table[data-testing-id="bo-product-list-page-organization"]') }
    }
}

class ProductListChannelPage extends ProductListPage
{
    static content= {
        contentSlot         { $('table[data-testing-id="page-bo-product-list-channel"]') }
    }
}

class ProductListChannelResellerPage extends ProductListPage
{
    static content= {
        contentSlot         { $('table[data-testing-id="page-bo-product-list-partner"]') }
    }
}

class ProductListPage extends BackOfficePage
{
    static at =
    {
       waitFor(30){ contentSlot.size()>0 }
    }

    static content= {
        contentSlot         { $('[data-testing-id^="page-bo-product-list"]') }
        
        searchForm          { $("form", name:"SimSearch") }
        detailForm          { $("form", name:"detailForm") }
        deleteForm          (required: true) { $("form", name:"deleteForm") }
        
        searchInput         { searchForm.$('input', id: 'WFSimpleSearch_NameOrID') }
        showAllInput(required: false) { detailForm.$('input', name: 'PageSize_-1') }
        
        btnFind             { searchForm.$('input', name: 'findSimple') }
        btnNew(to: ProductNewPage)              { detailForm.$('input', name: 'new') }
        btnDelete           { detailForm.$('input', name: 'confirmDelete') }
        btnOk               { detailForm.$('input', name: 'Assign') }
        btnCancel           { detailForm.$('input', name: 'cancel') }
        btnDeleteCancel(required: false) { deleteForm.$('input', name: 'cancel') }
        
        
        productList         { $('.products_list_row').collect{ it.module(ProductListEntry) } }
    }


    //------------------------------------------------------------
    // link functions
    //------------------------------------------------------------
    def clickFind()
    {
        btnFind.click()

    }

    def clickNew()
    {
        btnNew.click()
    }

    def clickDelete()
    {
        btnDelete.click()
    }

    def clickOK()
    {
        btnOK.click()
    }

    def clickCancel()
    {
        btnCancel.click()
    }

    def clickDeleteOK()
    {
        waitFor{$("input", name: "delete").size()>0}

        $("input", name: "delete").click()
    }

    def clickDeleteCancel()
    {
        btnDeleteCancel.click()
    }

    def searchProduct(productNameOrID)
    {
        searchInput.value  productNameOrID
        btnFind.click()
        // TODO: Find a way to use waitFor to determine when page is reloaded(add info box, etc.)
        sleepForNSeconds(5)
        waitFor{detailForm.size()>0}
        
        if(showAllInput.size()>0)
        {
            showAllInput.click()
        }
        
        waitFor{$("a", text: contains(productNameOrID)).size()>0}
    }
    
    def openProduct(productNameOrID)
    {
        $("a", text:productNameOrID)[0].click()        
    }   

    def selectProductBySKU(productSKU)
    {
        def product = productList.findAll().find{

            it.sku == productSKU
            
        }
        
        if( product != null )
        {
            product.selected = true
        }
        else
            println "not found"
       
    }   

    def deleteProduct(sku)
    {
        searchProduct sku
        selectProductBySKU sku
        clickDelete()
        clickDeleteOK()
    }
    
    def findAndAcceptProduct(sku)
    {
        searchProduct sku
        selectProductBySKU sku
        clickOK()
    }

}

