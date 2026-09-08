package geb.com.intershop.inspired.pages.backoffice

import geb.Page
import geb.com.intershop.b2c.model.storefront.responsive.User

class BackOfficeLoginPage extends Page
{
    static url = "/INTERSHOP/web/WFS/SLDSystem"
    static at=
    {
        waitFor { $("div", "data-testing-id":"page-bo-login") }
    }

    static content =
    {
        loginInput        { $('input', id: 'LoginForm_Login') }
        passwordInput     { $('input', id: 'LoginForm_Password') }
        organizationInput { $('input', id: 'LoginForm_RegistrationDomain') }
        loginButton(to: [BackOfficePage, BackOfficeLoginPage]) { $('input', "data-testing-id":'btn-login') }
    }
    
    def loginUser(user,password, organization)
    {
        loginInput.value   user
        passwordInput.value   password
        organizationInput.value organization
        
        loginButton.click()

    }

    BackOfficePage logInUser(User user, organization)
    {
        loginUser(user.email, user.password, organization)
        browser.page
    }   
}