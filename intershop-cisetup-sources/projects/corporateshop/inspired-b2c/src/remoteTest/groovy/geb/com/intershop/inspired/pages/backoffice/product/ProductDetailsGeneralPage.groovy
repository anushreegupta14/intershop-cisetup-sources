package geb.com.intershop.inspired.pages.backoffice.product

import geb.Page
import geb.com.intershop.inspired.pages.backoffice.BOLoginPage

class ProductDetailsGeneralPage extends ProductDetailsPage
{
    static at =
            {
                waitFor{ contentSlot.size()>0 }
            }

    static content= {

        contentSlot                 { $('table[data-testing-id^="page-bo-product-edit-general-tab"]') }

        localeForm 					{ $("form", name:"setLocale") }

        prodNameInput				{ $("input",id:"UpdateProduct_ProductName") }
        prodIDInput					{ $("input",id:"UpdateProduct_ProductID") }

        prodIsOnlineInput			{ $("input",id:"UpdateProduct_ProductOnlineStatus") }
        prodIsAvailableInput		{ $("input",id:"UpdateProduct_ProductAvailableStatus") }
        prodWarrantyEligibleInput	{ $("input",id:"UpdateProduct_WarrantyEligible") }

        btnApplyLanguage			{ $('input', name: 'apply') }

        btnApplyUpdate				{ $('input', name: 'update') }
        btnUnlock					{ $('input', name: 'unlock') }
        btnReset					{ $('input', name: 'reload') }
        btnCopy						{ $('input', name: 'copy') }
        btnDelete					{ $('input', name: 'confirmDelete') }
        btnBackToList                   { $('input', name: 'back')}

        categoriesForm  			{ $("form", name:"assignedCategoriesForm") }
        btnAssign					{ $("input",name:"ManageCategories") }

    }

    def setNameAndID(prodName,prodID)
    {
        prodNameInput.value prodName
        prodIDInput.value prodID
    }

    def setOnline(value)
    {
        prodIsOnlineInput.value value
    }

    def setAvailable(value)
    {
        prodIsAvailableInput.value value
    }

    def setWarrantyEligibleInput(value)
    {
        prodWarrantyEligibleInput.value value
    }

    def clickBackToList()
    {
        btnBackToList.click()
    }
}