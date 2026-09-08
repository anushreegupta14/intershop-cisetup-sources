package geb.com.intershop.inspired.pages.storefront.responsive.account.subscriptions

import geb.com.intershop.inspired.pages.storefront.responsive.account.AccountPage
import geb.com.intershop.inspired.pages.storefront.responsive.modules.SubscriptionItem

class SubscriptionsPage extends AccountPage
{

    static at =
    {
        waitFor { subscriptionsList.displayed }
    }

    static content = {
        subscriptionsList(required: true) {
            $('table[data-testing-id="subscriptionsList"] tbody tr')*.module(SubscriptionItem)
        }
    }

    def openDetails(number)
    {
        def item = subscriptionsList.find{ it.subscriptionNumber == number }
        assert item != null
        item.openDetails()
    }

}
