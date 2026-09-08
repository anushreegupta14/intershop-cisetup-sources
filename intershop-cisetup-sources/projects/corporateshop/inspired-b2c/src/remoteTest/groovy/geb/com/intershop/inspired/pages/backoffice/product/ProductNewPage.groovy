package geb.com.intershop.inspired.pages.backoffice.product


class ProductNewPage extends ProductDetailsPage
{
    static at =
    {
       waitFor{ contentSlot.size()>0 }
    }

    static content= {
        contentSlot                 { $('table[data-testing-id^="page-bo-product-new"]') }

		localeForm   				{ $("form", name:"localeForm") }

        prodNameInput				{ $("input",name:"CreateProduct_ProductName") }
        prodIDInput					{ $("input",id:"CreateProduct_ProductID") }
		
        prodIsOnlineInput			{ $("input",id:"CreateProduct_ProductOnlineStatus") }
        prodIsAvailableInput		{ $("input",id:"CreateProduct_ProductAvailableStatus") }
        prodWarrantyEligibleInput	{ $("input",name:"CreateProduct_WarrantyEligible") }

        btnApplyLanguage			{ $('input', name: 'apply') }
        btnApplyCreate(to: ProductDetailsGeneralPage)				{ $('input', name: 'create') }
        btnCancel					{ $('input', name: 'cancel') }
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

	
}

