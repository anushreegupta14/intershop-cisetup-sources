package geb.com.intershop.inspired.pages.backoffice.product


class ProductDetailsPricingWarrantyFixedPage extends ProductDetailsPricingWarrantyPage
{
    static at =
    {
       waitFor(5, 0.1){ contentSlot.size()>0 }
    }

    static content= {
    
        contentSlot                         { $('form[data-testing-id="bo-product-classification-warranty-fixed-price-config"]') }

        warrantyFixPriceCurrencyForm        { contentSlot }
        warrantyFixPriceCurrencySelector    { warrantyFixPriceCurrencyForm.$("select",name:"FixedPriceWarranty_Price_PriceCurrency") }
        
        warrantyFixPriceValueInput          { warrantyFixPriceCurrencyForm.$("input",name:"FixedPriceWarranty_Price_PriceValue") }

        btnApplyPercentageOrScaledValue     { warrantyFixPriceCurrencyForm.$('input', name: 'apply') }
        btnAddPriceEntry(to: ProductDetailsPricingWarrantyFixedPage)                    { warrantyFixPriceCurrencyForm.$('input', name: 'add') }

    }
    
    def selectWarrantyFixPriceCurrencyByName (cName)
    {
        warrantyFixPriceCurrencyForm.click()
        warrantyFixPriceCurrencySelector.find("option").find{
            it.text().trim() == cName.trim()
        }.click()
    }

    def setWarrantyFixPriceValue(warrantyPriceValue)
    {
        warrantyFixPriceValueInput.value warrantyPriceValue
    }

    def clickAddPriceEntry()
    {
        btnAddPriceEntry.click()

    }

    def clickApplyPercentageOrScaledValue()
    {
        btnApplyPercentageOrScaledValue.click()
    }

}

