package geb.com.intershop.inspired.pages.backoffice.product

class ProductDetailsAttachmentsPage extends ProductDetailsPage
{

    static at =
    {
       waitFor{ contentSlot.size()>0 }
    }
    
    
    static content= {
        contentSlot                 { $('table[data-testing-id^="page-bo-product-details-channel"]') }
        btnBackToList               { $('input', name: 'back') }
    }
    
    
    def selectAttachmentByName (cName)
    {
        $("a", text: contains(cName)).click()
    }
}
