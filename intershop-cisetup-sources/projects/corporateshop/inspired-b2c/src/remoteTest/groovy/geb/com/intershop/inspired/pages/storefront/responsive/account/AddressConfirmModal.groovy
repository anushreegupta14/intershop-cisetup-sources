package geb.com.intershop.inspired.pages.storefront.responsive.account

import geb.com.intershop.inspired.pages.storefront.responsive.StorefrontPage;

class AddressConfirmModal extends StorefrontPage
{
    static at =
    {
        sleepForNSeconds(3)
        waitFor{confirmButton.size()>0}
    }

    static content =
    {
        confirmButton { $("button",type:"submit",value:"delete") }
    }
    
}
