package geb.com.intershop.inspired.pages.storefront.responsive.account

import geb.Page
import geb.com.intershop.inspired.pages.storefront.responsive.account.AccountPage

class AccountNavigationPage extends AccountPage
{
    static at =
    {
        waitFor{ navigation.displayed }
    }

    static content=
    {
        navigation { $('[data-testing-id="myaccount-navigation"]') }
        subscriptionsLink { navigation.$('[data-testing-id="myaccount-subscriptions-link"]') }
    }


    def openSubscriptions()
    {
        subscriptionsLink.click()
    }

}
