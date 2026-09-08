package geb.com.intershop.inspired.pages.backoffice.catalog

import geb.com.intershop.inspired.pages.backoffice.BackOfficePage

class CatalogCategoryOverviewPage extends BackOfficePage
{
    static at = {
        waitFor{ tableAldiBox.size()>0 }
    }
    static content= {
        
        editCategoryForm    { $("form", name:"editCategoryForm") }

        rootCategoryForm    { $("form", name:"rootCategoryForm") }
        btnPreview          { rootCategoryForm.$('input', name: 'previewThisCategory') }
        
        categoryForm        { $("form", name:"CategoryForm") }
        productForm         { $("form", name:"productForm") }
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
    
    def previewCatalog()
    {
        btnPreview.click()
    }
    
    def editCatalogOrViewProperties()
    {
        btnEditProperties.click()
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
