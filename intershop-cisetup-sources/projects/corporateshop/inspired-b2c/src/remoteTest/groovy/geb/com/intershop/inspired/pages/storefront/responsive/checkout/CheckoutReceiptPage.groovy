package geb.com.intershop.inspired.pages.storefront.responsive.checkout

import geb.com.intershop.inspired.pages.storefront.responsive.StorefrontPage
import geb.com.intershop.inspired.pages.storefront.responsive.modules.OrderInvoiceAddressSlot;
import geb.com.intershop.inspired.pages.storefront.responsive.modules.OrderShippingAddressSlot;

class CheckoutReceiptPage extends StorefrontPage
{
    static at=
    {
        waitFor{ contentSlot.displayed }
    }

    static content =
    {
        contentSlot { $('[data-testing-id="checkout-receipt-page"]') }
        orderInvoiceAddressSlot { module OrderInvoiceAddressSlot }
        orderShippingAddressSlot { module OrderShippingAddressSlot }
        myAccountLink { $('[data-testing-id="myaccount-link"]') }
        orderNumber { contentSlot.$('[data-testing-id="order-document-number"]').text() }
    }

    def openMyAccount() {
        myAccountLink.click()
    }

}
