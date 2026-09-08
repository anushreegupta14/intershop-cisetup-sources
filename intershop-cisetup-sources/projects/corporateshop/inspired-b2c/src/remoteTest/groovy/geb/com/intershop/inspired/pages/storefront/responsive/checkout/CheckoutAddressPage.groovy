package geb.com.intershop.inspired.pages.storefront.responsive.checkout

import geb.com.intershop.inspired.pages.storefront.responsive.StorefrontPage
import geb.com.intershop.inspired.pages.storefront.responsive.modules.OrderAddressSummary
import geb.com.intershop.inspired.pages.storefront.responsive.modules.ProgressBar
import geb.module.*

class CheckoutAddressPage extends StorefrontPage
{
    static at=
    {
        waitFor{contentSlot.size()>0}
    }

    static content =
    {
        contentSlot { $('div[data-testing-id="checkout-address-page"]') }
        continueButton { $('a',href: contains("ViewCheckoutAddresses-ContinueCheckout"))}
        orderAddressSummary { module OrderAddressSummary }
        invoiceAddressSelector {$("select", id:"InvoiceAddressID").module(Select)}
        shippingAddressSelector {$("select", id:"ShippingAddressID").module(Select)}
        invoiceAddressText {$('div[data-testing-id="invoice-address-slot"]').$("address").text().trim()}
        shippingAddressText {$('div[data-testing-id="shipping-address-slot"]').$("address").text().trim()}
        navigationBar {module ProgressBar,$('div[data-testing-id="progress-bar"]')}
    }
    
    def continueClick()
    {
        continueButton.click()
    }
    
    def selectInvoiceAddress(def addressName)
    {
        invoiceAddressSelector.selected = addressName
        sleepForNSeconds(5)
    }
    
    def selectShippingAddress(String addressName)
    {
        shippingAddressSelector.selected = addressName
        sleepForNSeconds(5)
    }
}
