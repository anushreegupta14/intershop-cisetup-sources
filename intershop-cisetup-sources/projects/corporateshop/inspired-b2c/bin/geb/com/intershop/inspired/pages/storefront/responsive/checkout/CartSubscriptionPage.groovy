package geb.com.intershop.inspired.pages.storefront.responsive.checkout

import geb.module.*
import geb.com.intershop.inspired.pages.storefront.responsive.checkout.CheckoutPaymentPage

class CartSubscriptionPage extends CartPage
{

    static at = {
        waitFor{ cartSubscriptionSection.displayed }
    }

    static content = {

        cartSubscriptionSection         { $('[data-testing-id="cartSubscriptionSection"]') }
            basketTypeSelector          { cartSubscriptionSection.$('[name="CartToSubscriptionSwitch"]').module(RadioButtons) }
            cartSubscriptionForm        { cartSubscriptionSection.$('[data-testing-id="cartSubscriptionForm"]') }
                subscriptionCount           { cartSubscriptionSection.$('[data-testing-id="subscriptionRecurrenceCount"]') }
                subscriptionInterval        { cartSubscriptionSection.$('[data-testing-id="subscriptionInterval"]').module(Select) }
                subscriptionStartDate       { cartSubscriptionSection.$('[data-testing-id="subscriptionStartDate"]').module(TextInput) }
                subscriptionEndDate         { cartSubscriptionSection.$('[data-testing-id="subscriptionEndDate"]').module(TextInput) }
        subscriptionCheckoutButton(to: CheckoutPaymentPage, required:true) { $('[data-testing-id="subscription-checkout-btn"]') }

        datepicker  { $('.datepicker') }

    }

    def selectOnetimepurchace() {
        basketTypeSelector.checked = "default"
    }

    def selectSubscription() {
        basketTypeSelector.checked = "subscription"
        waitFor{ cartSubscriptionForm.displayed }
    }

    def defineSubscriptionValues(count, interval, startDate, endDate) {
        subscriptionCount.value(count)
        subscriptionInterval.selected = interval
        subscriptionStartDate.text = startDate
        subscriptionEndDate.text = endDate
    }

    def subscriptionCheckout() {
        subscriptionCheckoutButton.click()
    }

}
