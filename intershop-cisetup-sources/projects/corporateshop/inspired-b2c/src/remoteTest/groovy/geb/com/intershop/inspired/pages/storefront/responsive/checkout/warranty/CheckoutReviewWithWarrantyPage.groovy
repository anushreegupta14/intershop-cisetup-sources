package geb.com.intershop.inspired.pages.storefront.responsive.checkout.warranty

import geb.com.intershop.inspired.pages.storefront.responsive.checkout.CheckoutReviewPage

class CheckoutReviewWithWarrantyPage extends CheckoutReviewPage
{
    static at=
    {
        waitFor{warrantyDetails.size()>0}
    }

    static content =
    {
        warrantyDetails { $("div[data-testing-id='checkout-pli-warranty-details']") }
    }
    
    //------------------------------------------------------------
    // Page checks
    //------------------------------------------------------------
    def checkLineItemWithWarranty(warrantyName)
    {
        warrantyDetails.text().startsWith(warrantyName)
    }
}
