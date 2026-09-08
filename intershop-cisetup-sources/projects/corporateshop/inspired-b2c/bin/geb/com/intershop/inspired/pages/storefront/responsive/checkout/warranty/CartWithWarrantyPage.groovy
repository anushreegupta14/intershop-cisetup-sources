package geb.com.intershop.inspired.pages.storefront.responsive.checkout.warranty

import geb.com.intershop.inspired.pages.storefront.responsive.checkout.CartPage

import geb.Page

class CartWithWarrantyPage extends CartPage
{
    static at =
    {
        waitFor{ warrantyProductsSelectBox.size()>0}
    }

    static content =
    {
        warrantyProductsSelectBox { $('select[data-testing-id="checkout-pli-warranty-selection"]') }
    }

    //------------------------------------------------------------
    // link functions
    //------------------------------------------------------------
    def selectWarrantyProductInDropDown(warrantySku)
    {
        warrantyProductsSelectBox.value("Warranty_" + warrantySku)
        // warranty 'drop down box'.select triggers a page reload
    }
}