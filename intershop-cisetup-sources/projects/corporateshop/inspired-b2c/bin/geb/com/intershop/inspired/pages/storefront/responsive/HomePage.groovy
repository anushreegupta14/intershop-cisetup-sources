package geb.com.intershop.inspired.pages.storefront.responsive

import geb.com.intershop.inspired.pages.storefront.responsive.shopping.CategoryPage

class HomePage extends StorefrontPage
{


    static url= StorefrontPage.url + "ViewHomepage-Start";
    
    static at =
    {
        //The homepage must get the class "homepage" in backoffice
        waitFor{$("body",class:"homepage").size()>0}
    }

    static content =
    {
        signInLink(required:false){$("a",class:"my-account-links my-account-login")}
        catalogBar {$("ul",class:contains("navbar-nav"))}
        registerLink(required:false) {$("a",class:"ish-siteHeader-myAccountUtilitiesMenu-myAccount-register")}
        logoutLink(required:false) {$("a",class:"my-account-link my-account-logout")}
        
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

    def pressLogOut()
    {
        logoutLink.click()
    }

    def selectCatalog(channel)
    {
        $("a[data-testing-id='"+channel+"-link']").click()
    }

    def clickCategoryLink(categoryId) {
        $('[data-testing-id="'+categoryId+'-link"]').click(CategoryPage)
        waitFor{$("div", "class": "category-page", "data-testing-id":categoryId).size()>0}
    }
}

