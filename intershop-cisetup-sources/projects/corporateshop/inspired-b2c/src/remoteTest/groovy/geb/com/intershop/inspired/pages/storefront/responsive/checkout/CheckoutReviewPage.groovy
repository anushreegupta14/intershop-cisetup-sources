package geb.com.intershop.inspired.pages.storefront.responsive.checkout

import geb.com.intershop.inspired.pages.storefront.responsive.StorefrontPage
import geb.com.intershop.inspired.pages.storefront.responsive.modules.OrderInvoiceAddressSlot;
import geb.com.intershop.inspired.pages.storefront.responsive.modules.OrderShippingAddressSlot;
import geb.module.*

class CheckoutReviewPage extends StorefrontPage
{

    static at=
    {
        waitFor{ contentSlot.displayed }
    }

    static content =
    {
        contentSlot { $("[data-testing-id='checkout-review-page']") }
        agreeCheckBox { $("[id='terms-conditions-agree']").module(Checkbox) }
        submitButton { $("button",name:"sendOrder")}
        orderInvoiceAddressSlot { module OrderInvoiceAddressSlot }
        orderShippingAddressSlot { module OrderShippingAddressSlot }

        recurringInformationInfo { $('[data-testing-id="recurring-information-info"]') }
		pliDesiredDeliveryDate   { $("span[data-testing-class='pli-desiredDeliveryDate']")}

    }

    def checkTermsAndConditions() {
        agreeCheckBox.check()
    }

    def continueCheckout() {
        submitButton.click()
    }

    def isRecurringInformationInfoDisplayed() {
        return recurringInformationInfo.displayed
    }

	def isDesiredDeliveryDateDisplayed() {
		return pliDesiredDeliveryDate.displayed
	}
}
