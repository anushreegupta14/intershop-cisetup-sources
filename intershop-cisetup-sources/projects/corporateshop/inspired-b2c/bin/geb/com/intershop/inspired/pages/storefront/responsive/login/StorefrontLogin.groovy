package geb.com.intershop.inspired.pages.storefront.responsive.login

import geb.com.intershop.inspired.pages.storefront.responsive.HomePage
import geb.com.intershop.inspired.pages.storefront.responsive.account.AccountLoginPage
import geb.com.intershop.inspired.pages.storefront.responsive.account.AccountPage

/**
 * Trait used to login at the storefront.
 */
trait StorefrontLogin
{
    AccountPage logInStorefrontUser(String email, String password) {
        when: "I go to Homepage"
            to HomePage
            
        and: "click login link"
            pressLogIn()
            
        and: "provide the login data"
            at AccountLoginPage
            login email, password
            
        then: "I am logged in"
            at AccountPage
            browser.page
    }

    HomePage logOutStorefrontUser() {
        when: "I go to Homepage"
            to HomePage
            
        and: "click the logout link"
            pressLogOut()
            
        then: "I am logged out"
            at HomePage
            isSignedIn false
            browser.page
    }
}