package geb.com.intershop.inspired.pages.storefront.responsive.checkout

import geb.com.intershop.inspired.pages.storefront.responsive.StorefrontPage
import geb.com.intershop.inspired.pages.storefront.responsive.modules.OrderAddressSummary
import geb.com.intershop.inspired.pages.storefront.responsive.modules.ProgressBar

class CheckoutShippingPage extends StorefrontPage
{
    static at=
    {
        waitFor{contentSlot.size()>0}
        checkoutShippingMethods.size()>0
    }

    static content =
    {
        navigationBar {module ProgressBar,$('div[data-testing-id="progress-bar"]')}
        
        contentSlot { $('div[data-testing-id="checkout-shipping-page"]') }
        checkoutShippingMethods {$('div',class:'shipping-methods')}
        continueButton { contentSlot.$('button',name: "continue") }
        desiredDeliveryInput { contentSlot.$('input[data-testing-id="desiredDeliveryDate"]') }
        categoryName  { contentSlot.@"data-testing-id" }
        orderAddressSummary { module OrderAddressSummary }
    }
    //------------------------------------------------------------
    // link functions
    //------------------------------------------------------------
    def continueClick()
    {
        continueButton.click()
    }

    def fillData(desiredDelivery)
    {
        desiredDeliveryInput.value   desiredDelivery
    }
}
