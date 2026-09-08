package geb.com.intershop.inspired.pages.backoffice.product


class ProductDetailsPricingWarrantyPercentagePage extends ProductDetailsPricingWarrantyPage
{
    static at =
    {
       waitFor(5, 0.1){ contentSlot.size()>0 }
    }

    static content= {
    
        contentSlot                         { $('form[data-testing-id="bo-product-classification-warranty-percentage-price-config"]') }

        warrantyPercentageForm              { contentSlot }
        warrantyPercentageValueInput        { warrantyPercentageForm.$("input",name:"PercentagePriceWarranty_Percentage") }

        btnApplyPercentageValue(to: ProductDetailsPricingWarrantyPercentagePage)             { warrantyPercentageForm.$('input', name: 'apply') }
        btnResetPercentageValue             { warrantyPercentageForm.$('input', name: 'reset') }
        
    }
    
    def setWarrantyPercentageValue(warrantyPercentageValue)
    {
        warrantyPercentageValueInput.value warrantyPercentageValue
    }

    def clickApplyPercentageValue()
    {
        btnApplyPercentageValue.click()
    }

    def clickResetPercentageValue()
    {
        btnResetPercentageValue.click()
    }

}

