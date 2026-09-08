package geb.com.intershop.inspired.pages.storefront.responsive.shopping

import geb.com.intershop.inspired.pages.storefront.responsive.StorefrontPage;

class ExpressShopModal extends StorefrontPage
{
    static at =
    {
        sleepForNSeconds(10)
        waitFor{addToCartButton.size()>0}
    }

    static content =
    {
        addToCartButton { $("button[data-expresscart='']",name: "addProduct") }
        compareButton {sku-> $("a[data-testing-id='compare-"+sku+"']") }
    }

    def sleepForNSeconds(int n)
    {
        Thread.sleep(n * 1000)
        return true
    }
}
