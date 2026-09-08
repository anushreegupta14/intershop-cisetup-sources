package geb.com.intershop.inspired.pages.storefront.responsive.smb

import geb.com.intershop.inspired.pages.storefront.responsive.StorefrontPage
import geb.com.intershop.inspired.pages.storefront.responsive.account.AccountLoginPage;

import geb.Page

class AccountLoginPageSMB extends StorefrontPage
{
    static url= StorefrontPageSMB.url + AccountLoginPage.url
    static content =
    {
        loginInput {$('input', id:'ShopLoginForm_Login')}
        passwordInput {$('input', id:'ShopLoginForm_Password')}
        loginButton {$('button', name:'login')}
    }

    static at =
    {
        waitFor{$("div[data-testing-id='account-login-page']").size()>0}
    }

    //------------------------------------------------------------
    // Page checks
    //------------------------------------------------------------

    //------------------------------------------------------------
    // link functions
    //------------------------------------------------------------

    def login(user,password)
    {
        loginInput.value   user
        passwordInput.value   password
        loginButton.click()

    }
}
