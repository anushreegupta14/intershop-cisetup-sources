package geb.com.intershop.inspired.pages.storefront.responsive.modules

import geb.Module

class ProgressBar extends Module
{
    static content =
    {
        addressPageLink { $("a",href:contains("ViewCheckoutAddresses"))}
        shippingPageLink { $("a",href:contains("ViewCheckoutShipping"))}
        paymentPageLink { $("a",href:contains("ViewCheckoutPayment"))}
    }
    
    def gotoCheckoutAddressPage()
    {
        addressPageLink.click()
    }
    
    def gotoCheckoutShippingPage()
    {
        shippingPageLink.click()
    }
    
    def gotoCheckoutPaymentPage()
    {
        paymentPageLink.click()
    }
}
