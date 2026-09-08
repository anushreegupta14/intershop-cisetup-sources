package geb.com.intershop.inspired.pages.storefront.responsive.account.subscriptions

import geb.com.intershop.inspired.pages.storefront.responsive.account.AccountPage

class SubscriptionDetailsPage extends AccountPage
{
    static at =
    {
        waitFor {
            subscriptionDetails.displayed
        }
    }

    static content = {
        subscriptionDetails (required: true) { $('[data-testing-id="subscription-details"]') }
        subscriptionNumber (required: true) { $('[data-testing-id="subscription-number"]').text().trim() }
    }

}
