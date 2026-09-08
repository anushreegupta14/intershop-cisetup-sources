package geb.com.intershop.inspired.pages.backoffice.product


class ProductDetailsPricingWarrantyPage extends ProductDetailsPage
{
    static at =
    {
       waitFor(5, 0.1){ contentSlot.size()>0 }
    }

    static content= {
        
        contentSlot                         { $('table[data-testing-id^="page-bo-product-warranty-pricing"]') }

        priceProviderForm                   { $("form", name:"PriceProviderForm") }
        priceProviderSelector               { priceProviderForm.$("select",name:"PriceProviderName") }
      
        btnApplyPriceProvider(to: [ProductDetailsPricingWarrantyFixedPage, ProductDetailsPricingWarrantyPercentagePage, ProductDetailsPricingWarrantyScaledPage])               { priceProviderForm.$('input', name: 'apply') }

    }
    
    def selectWarrantyPriceProviderByName (cName)
    {
        priceProviderForm.click()
        priceProviderSelector.find("option").find{
            it.text().trim() == cName.trim()
        }.click()
    }

    def clickApplyPriceProvider()
    {
        btnApplyPriceProvider.click()

    }

}

