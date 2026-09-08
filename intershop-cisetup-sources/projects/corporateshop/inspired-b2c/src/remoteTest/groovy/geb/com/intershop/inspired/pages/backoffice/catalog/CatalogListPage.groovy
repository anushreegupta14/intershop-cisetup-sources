package geb.com.intershop.inspired.pages.backoffice.catalog

import geb.com.intershop.inspired.pages.backoffice.BackOfficePage

class CatalogListPage extends BackOfficePage
{
    static content= {
        createForm          { $("form", name:"createForm") }
        deleteForm          { $("form", name:"deleteCategoryForm") }
        
        btnNew              { createForm.$('input', name: 'new') }
        btnDeleteOK         { deleteForm.$('input', name: 'delete') }
        btnDeleteCancel     { deleteForm.$('input', name: 'backToList') } 
        
    }
    
    def catalogLink(catalogNameOrID)
    {
        $("a", href: contains("ViewChannelCatalog_52-Browse"), text: catalogNameOrID)
    }
    
    def openCatalog(catalogNameOrID)
    {
        catalogLink(catalogNameOrID).click()
    }
    
    def deleteCatalog(catalogNameOrID)
    {
       catalogLink(catalogNameOrID).parent().siblings().$("img", title: "Delete").click()
       clickDeleteOK()
    }
    
    def previewCatalog(catalogNameOrID)
    {
        catalogLink(catalogNameOrID).parent().siblings().$("a", href: contains("ViewChannelCatalog_52-PreviewCategory")).click()
    }
    
    def editCatalogByEditLink(catalogNameOrID)
    {
        catalogLink(catalogNameOrID).parent().siblings().$("img", title: "Edit").click()
    }
    
    def clickNew()
    {
        btnNew.click();
    }
    
    def clickDeleteOK()
    {
        btnDeleteOK.click()
    }

    def clickDeleteCancel()
    {
        btnDeleteCancel.click()
    }
    
}
