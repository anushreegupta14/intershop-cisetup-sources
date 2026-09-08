package geb.com.intershop.inspired.pages.storefront.responsive.checkout.variation

import geb.com.intershop.inspired.pages.storefront.responsive.checkout.CartPage

import geb.Page

class CartWithVariationPage extends CartPage
{
    static at =
    {
        waitFor{ editVariationLink.size()>0}
    }

    static content =
    {
        editVariationLink { $('a[title="Edit"]') }
        
        variationlist { $('dl[id="editProductVariation1"]')}
    }

    //------------------------------------------------------------
    // link functions
    //------------------------------------------------------------

    def isVariationSelected(variation)
    {
        return $("dd", text: contains(variation)).size() > 0
    }


}