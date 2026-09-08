package geb.com.intershop.inspired.pages.backoffice.product


class ProductDetailsPricingWarrantyScaledPage extends ProductDetailsPricingWarrantyPage
{
    static at =
    {
       waitFor(5, 0.1){ contentSlot.size()>0 }
    }

    static content= {
    
        contentSlot                         { $('form[data-testing-id="bo-product-classification-warranty-scaled-price-config"]') }

        warrantyScaledPriceCurrencyForm     { contentSlot }
        warrantyScaledPriceCurrencySelector { warrantyScaledPriceCurrencyForm.$("select",name:"ScaledPriceWarranty_Threshold_ThresholdCurrency") }
        
        warrantyScaledPriceThresholdValue   { warrantyScaledPriceCurrencyForm.$("input",name:"ScaledPriceWarranty_Threshold_ThresholdValue") }
        warrantyScaledPricePriceValue       { warrantyScaledPriceCurrencyForm.$("input",name:"ScaledPriceWarranty_Price_PriceValue") }
        
        btnAddPriceEntry                    { warrantyScaledPriceCurrencyForm.$('input', name: 'add') }
        btnApplyScaledValues(to: ProductDetailsPricingWarrantyScaledPage)                { warrantyScaledPriceCurrencyForm.$('input', name: 'apply') }

    }
    
    def selectWarrantyScaledPriceCurrencyByName (cName)
    {
        warrantyScaledPriceCurrencyForm.click()
        warrantyScaledPriceCurrencySelector.find("option").find{
            it.text().trim() == cName.trim()
        }.click()
    }

    def setWarrantyScaledPriceValue(thresholdPriceValue, warrantyPriceValue)
    {
        warrantyScaledPriceThresholdValue.value thresholdPriceValue
        warrantyScaledPricePriceValue.value warrantyPriceValue
    }

    def clickAddPriceEntry()
    {
        btnAddPriceEntry.click()

    }

    def clickApplyScaledValues()
    {
        btnApplyScaledValues.click()
    }

}

