package geb.com.intershop.inspired.pages.backoffice.product

class ProductDetailsAttachmentDetailsPage extends ProductDetailsPage
{

    static at =
    {
       waitFor{ inEmailFlag.size()>0 }
    }
    
    
    static content= {
        contentSlot                 { $('table[data-testing-id^="bo-sitenavbar-applications"]') }
        inEmailFlag                 { $("input", name:"RegForm_InEmail") }
        applyButton                 { $("input", name:"confirmUpdate") }
        btnBackToList               { $('input', name: 'back') }
    }
    
    def selectInEmailFlag()
    {   
        if(!$("input",name:"RegForm_InEmail").value().equals("on")){
            $("input",name:"RegForm_InEmail").click()
            $("input",name:"confirmUpdate",value:"Apply").click()

        }
    }
    
    def deselectInEmailFlag()
    {
        if($("input",name:"RegForm_InEmail").value().equals("on")){
            $("input",name:"RegForm_InEmail").click()
            $("input",name:"confirmUpdate",value:"Apply").click()

        }
    }
    
    def clickBack()
    {
        btnBackToList.click()
    }
}
