package geb.com.intershop.inspired.pages.storefront.responsive.smb

import geb.com.intershop.inspired.pages.storefront.responsive.StorefrontPage
import geb.com.intershop.inspired.pages.storefront.responsive.HomePage

class HomePageSMB extends StorefrontPage
{

    static url= StorefrontPageSMB.url + "ViewHomepage-Start";

    static content =
    {
        signInLink(required:false){$("a",class:"my-account-links my-account-login")}
        catalogBar {$("ul",class:contains("navbar-nav"))}
        registerLink {$("a",class:"ish-siteHeader-myAccountUtilitiesMenu-myAccount-register")}
    }

    static at =
    {
        //The homepage must get the class "homepage" in backoffice
        waitFor{$("body",class:"homepage").size()>0}
    }

    //------------------------------------------------------------
    // Page checks
    //------------------------------------------------------------

    def isSignedIn(bool)
    {
        bool == (signInLink.size()==0)
    }

    //------------------------------------------------------------
    // link functions
    //------------------------------------------------------------
    def pressLogIn()
    {
        signInLink.click()
    }

    def clickCatalog(param){
        catalogBar.$("a",text:param).size()>0

    }


    def selectCatalog(channel)
    {
        $("a[data-testing-id='"+channel+"-link']").click()
    }

}


