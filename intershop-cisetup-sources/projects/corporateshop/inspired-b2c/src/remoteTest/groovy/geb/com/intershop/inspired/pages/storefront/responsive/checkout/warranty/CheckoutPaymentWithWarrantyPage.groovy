package geb.com.intershop.inspired.pages.storefront.responsive.checkout.warranty

import geb.com.intershop.inspired.pages.storefront.responsive.checkout.CheckoutPaymentPage


class CheckoutPaymentWithWarrantyPage extends CheckoutPaymentPage
{
    static at=
    {
        waitFor{lineItemWithWarranty.size()>0}
    }

    static content =
    {
        lineItemWithWarranty { $('div[data-testing-id="widgets-cart-summary-warranty"]') }
    }

    //------------------------------------------------------------
    // Page checks
    //------------------------------------------------------------
    def checkLineItemWithWarranty(warrantyName)
    {
        lineItemWithWarranty.text().startsWith(warrantyName)
    }
}
