package geb.com.intershop.inspired.pages.storefront.responsive.checkout

import geb.com.intershop.inspired.pages.storefront.responsive.StorefrontPage;

import geb.Page

class EmptyCartPage extends StorefrontPage
{
    static at =
    {
        waitFor{ contentSlot.size()>0}
    }

    static content =
    {
        contentSlot {$("div[class='empty-cart']")}
        continueShopping {$("a[test='CONTINUE SHOPPING']")}   
    }

}