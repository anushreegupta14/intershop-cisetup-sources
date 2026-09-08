package geb.com.intershop.inspired.pages.storefront.responsive.account

import geb.com.intershop.inspired.pages.storefront.responsive.StorefrontPage;

import geb.Page

class AccountLoginPage extends StorefrontPage
{
    static url="ViewUserAccount-ShowLogin"

    static at =
    {
        waitFor{contentSlot.size()>0}
    }
    static content =
    {
        contentSlot   { $("div[data-testing-id='account-login-page']") }
        loginInput    { $('input', id:'ShopLoginForm_Login') }
        passwordInput { $('input', id:'ShopLoginForm_Password') }
        loginButton   { $('button', name:'login') }
    }



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
