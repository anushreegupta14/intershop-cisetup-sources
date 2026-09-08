package geb.com.intershop.inspired.pages.backoffice.catalog

import geb.com.intershop.inspired.pages.backoffice.BOLoginPage

class CatalogGeneralTab extends BOLoginPage
{
    static at = {
        waitFor{ checkBoxShowInMenu.size()>0 }
    }
    static content= {
                
        checkBoxShowInMenu  { $("input",name:"RegFormEditCatalog_ShowInMenu") }
        btnApply            { $('input', name: 'update')}
    }
    
    def checkShowInMenuCheckbox()
    {
        checkBoxShowInMenu = true
                
    }
    
    def uncheckShowInMenuCheckbox()
    {
        checkBoxShowInMenu = false
                
    }
}
