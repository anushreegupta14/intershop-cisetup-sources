package geb.com.intershop.inspired.pages.storefront.responsive.checkout

import geb.com.intershop.inspired.pages.storefront.responsive.StorefrontPage

class RedirectBeforeDummyPaymentProviderPage extends StorefrontPage
{
    static at=
    {
        waitFor{contentSlot.size()>0}
    }
    
    static content =
    {
        contentSlot         { $('form[id="OnlinePayAccountForm"]') }
        applyPaymentButton      { contentSlot.$('button',name:"ok") }
    }
    
    
    def submitProviderRequest()
    {
        applyPaymentButton.click()
    }
}
