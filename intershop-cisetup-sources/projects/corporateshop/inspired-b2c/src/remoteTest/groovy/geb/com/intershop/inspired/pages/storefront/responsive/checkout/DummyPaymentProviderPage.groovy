package geb.com.intershop.inspired.pages.storefront.responsive.checkout

import geb.com.intershop.inspired.pages.storefront.responsive.StorefrontPage;

class DummyPaymentProviderPage extends StorefrontPage
{
    static at=
            {
                waitFor{applyButton.size()>0}
            }

    static content =
            {
                // common page element
                applyButton {$('button',name:"ok")}

                // only content for fast pay
                fastPayFirstPageContentSlot        { $('form[id="FastPayLoginForm"]') }
                applyFastPayButton      { fastPayFirstPageContentSlot.$('button',name:"ok") }
                fastPaySecondPageContentSlot      { $('form[name="FastPayLoginForm"]')}
                submitFastPayRequest   { fastPaySecondPageContentSlot.$('button',name:"ok") }

                // only content for redirect before
                contentSlot         { $('form[id="OnlinePayAccountForm"]') }
                applyRedirectBeforeButton      { contentSlot.$('button',name:"ok") }
                cancelLink           { contentSlot.$("a",class:"ipay-link-cancel")  }

                // only content for redirect after
                redirectAfterFirstPageContentSlot         { $('form[id="OnlinePayAccountForm"]') }
                applyButtonFirstPage      { redirectAfterFirstPageContentSlot.$('button',name:"ok") }
                redirectAfterSecondPageContentSlot      { $('form[id="OnlinePayPINForm"]')}
                applyButtonSecondPage   { redirectAfterSecondPageContentSlot.$('button',name:"ok") }
                redirectAfterThirdPageContentSlot      { $('form[name="Form"]')}
                applyButtonThirdPage      { redirectAfterThirdPageContentSlot.$('button',name:"back") }
            }


    def submitFastPayRequest()
    {
        applyFastPayButton.click()
        submitFastPayRequest.click()
    }
    
    def cancelRequest()
    {
        cancelLink[0].click()
    }


    def submitRedirectBeforeRequest()
    {
        applyRedirectBeforeButton.click()
    }

    def submitRedirectAfterRequest()
    {
        applyButtonFirstPage.click()
        applyButtonSecondPage.click()
        applyButtonThirdPage.click()
    }
}