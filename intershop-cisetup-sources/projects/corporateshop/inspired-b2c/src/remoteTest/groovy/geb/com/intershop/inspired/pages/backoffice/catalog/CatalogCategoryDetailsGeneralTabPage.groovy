package geb.com.intershop.inspired.pages.backoffice.catalog

import geb.com.intershop.inspired.pages.backoffice.catalog.CatalogCategoryDetailsPage

/**
 * This page describes the inner html page details of the "General" tab 
 * of the BO catalog category properties handling page.
 *
 */
class CatalogCategoryDetailsGeneralTabPage extends CatalogCategoryDetailsPage
{
    static content= {
    
        localeForm                  { $("form", name: "setLocale") }
        btnApplyLanguage            { localeForm.$('input', name: 'apply') }
        
        categoryForm                { $("form", name: "formMask") }
        catNameInput                { $("input", id: "RegFormAddCategory_DisplayName") }
        catIDInput                  { $("input", id: "RegFormAddCategory_Id") }
        
        catIsOnlineInput            { $("input", id: "RegFormAddCategory_IsOnline") }
        
        catParentCatIDInput         { $("input", name: "RegFormAddCategory_ParentId") }
        btnParentCatIDSelect        { $('input', name: 'selectParent') }
        
        btnApplyUpdate              { $('input', name: 'update') }
        btnReset                    { $('input', name: 'reset') }
        btnCopy                     { $('input', name: 'copyCategory') }
        btnDelete                   { $('input', name: 'confirmDelete') }
        
        categoriesForm              { $("form", name:"assignedCategoriesForm") }
        btnAssign                   { $("input",name:"ManageCategories") }
        
    }

    def setNameAndID(catName,catID)
    {
        catNameInput.value catName
        catIDInput.value catID
    }

    def setOnline(value)
    {
        catIsOnlineInput.value value
    }

    def clickSelectImageAssignment()
    {
        btnCatImageSelect.click()
    }

    def checkCategoryImageIsAssigned(directoryPath, imageName)
    {
        def reqImagePath = directoryPath + "/" + imageName
        def curImagePath = currentlyAssignedCategoryImage()
         
        assert curImagePath == reqImagePath
        assert categoryForm.$("a", href:endsWith(reqImagePath)).displayed
        
        return curImagePath == reqImagePath
    }
    
    def isPresentImageAssignmentButton()
    {
        return categoryForm.$("input", name: "browseDirectoryAdd").size()>0
    }
}

