package geb.com.intershop.inspired.specs.storefront.b2c.responsive

import geb.com.intershop.b2c.model.storefront.responsive.User
import geb.com.intershop.inspired.pages.backoffice.BackOfficeLoginPage
import geb.com.intershop.inspired.pages.backoffice.BackOfficePage
import geb.com.intershop.inspired.pages.backoffice.channels.BackOfficeChannelOverviewPage
import geb.com.intershop.inspired.pages.backoffice.services.BackOfficeServicesPage
import geb.com.intershop.inspired.pages.backoffice.services.ServicePage
import geb.com.intershop.inspired.pages.backoffice.services.ServiceSharingPage
import geb.com.intershop.inspired.pages.storefront.responsive.*
import geb.com.intershop.inspired.pages.storefront.responsive.account.*
import geb.com.intershop.inspired.pages.storefront.responsive.checkout.CartPage
import geb.com.intershop.inspired.pages.storefront.responsive.checkout.CheckoutPaymentPage
import geb.com.intershop.inspired.pages.storefront.responsive.checkout.CheckoutReceiptPage
import geb.com.intershop.inspired.pages.storefront.responsive.checkout.CheckoutReviewPage
import geb.com.intershop.inspired.pages.storefront.responsive.checkout.CheckoutShippingPage
import geb.com.intershop.inspired.pages.storefront.responsive.checkout.NewUserAddressPage
import geb.com.intershop.inspired.pages.storefront.responsive.checkout.variation.*
import geb.com.intershop.inspired.pages.storefront.responsive.shopping.*
import geb.com.intershop.inspired.testdata.TestDataUsage
import geb.spock.GebReportingSpec
import spock.lang.*

/**
 * Storefront test that verify correct format of the Google Tag Manager ecommerce events triggered
 * during the checkout process for Product with Variations at inSPIRED.
 * This allows measuring the ecommerce activities using Google Analytics Enhanced Ecommerce.
 * @see <a href="https://developers.google.com/tag-manager/enhanced-ecommerce">Enhanced Ecommerce (UA) Developer Guide</a>
 */
@Timeout(600)
class GoogleTagManager extends GebReportingSpec implements TestDataUsage
{
    def setupSpec() {
        setup:
            User b2cUser = new User("admin", "admin", "admin")
        when:
            to BackOfficeLoginPage
        then:
            at BackOfficeLoginPage
        // Activate the Google Tag Manager Service for the channel in the BackOffice
        when: "Logged in as admin into SMB Responsive Application"
            logInUser(b2cUser, "inSPIRED")
        and:
            at BackOfficePage
        when: "I switch to the SMB Responsive Application"
            selectChannel("inTRONICS")
        then:
            at BackOfficeChannelOverviewPage
        when: "Go to services"
            services.click()
        then:
            at BackOfficeServicesPage
        when: "Open  Google Tag Manager Service"
            openLocalService("Google Tag Manager")
        then:
           at ServicePage
           getServiceName() == "Google Tag Manager"
        when: "Open the Sharing tab of the service"
           openServiceSharingTab()
        then:
          at ServiceSharingPage
        when: "Make the service active and apply changes"
            selectSharedAndActive()
            applyChanges()
        then: "Service is Active"
            at ServiceSharingPage
               isSharedAndActive()
    }

    def "Track Ecommerce Activities during checkout process"() {
        when: "I go to the home page and search for an existing product..."
            to HomePage
        then:
            at HomePage
        when:
            header.search productId
        then: "Product Detail Page is shown"
            at ProductDetailPage
            lookedForSKU productId
        and: "View of Product Details ecommerce activity is trigerred"
            verifyProductDetailsActivity(productId)
        when: "I add product to Cart..."
            addToCart()
        then: "Shopping Cart page with variation is shown"
            at CartPage
            checkProduct productName
        and: "addToCart ecommerce activity for the product is trigerred"
            verifyAddToCartActivity(productId, "1")
        and: "Product impression ecommerce activity for a related product is trigerred"
            verifyProductImpressionActivity(suggestedProductId)
        when: "Increase product quantity and update the cart"
            // change quantity: set to 3 (add 2)
            contentSlot.$("input[data-testing-id='product-count-" + productName + "-element']").value "3"
            updateButton.click()
        then: "Shopping Cart page with updated quantity is shown"
            at CartPage
            productCartTable(productName).quantityInput.value() == "3"
        and : "addToCart ecommerce event for the product is trigerred with quantity 2"
            println "Increase product quantity in shopping cart"
            verifyAddToCartActivity(productId, "2")
        when: "Decrease product quantity and update the cart"
            // change quantity remove: set quantity to 1 (remove 2)
            contentSlot.$("input[data-testing-id='product-count-" + productName + "-element']").value "1"
            updateButton.click()
        then: "Shopping Cart page with upated quantity is shown"
            at CartPage
            productCartTable(productName).quantityInput.value() == "1"
        and: "removeFromCart ecommerce event for the product is trigerred with quantity 2"
            println "Decrease product quantity in shopping cart"
            verifyRemoveFromCartActivity(productId, "2")
        when: "Now I press the remove link and switch to the home page"
            productCartTable(productName).removeLink.click()
            to HomePage
        then:
            at HomePage
        when: "I go to the home page and search for an existing product..."
            header.search productVariationId
        then: "Product Detail Page is shown"
            at ProductDetailPage
            lookedForSKU productVariationId
        and: "View of Product Details ecommerce activity is trigerred"
            verifyProductDetailsActivity(productVariationId)
        when: "I add product to Cart..."
            addToCart()
        then: "Shopping Cart page with variation is shown"
            at CartPage
            checkProduct productName
        and: "addToCart/removefromCart ecommerce activity is trigerred"
            println "Product variation changed in shopping cart"
            verifyAddToCartActivity(productVariationId, "1")
        when: "Now I will checkout..."
            checkOut()
        then: "Address page is shown"
            at NewUserAddressPage
        and : "Checkout step 1 ecommerce event for the product is trigerred"
            verifyCheckoutStepActivity(productVariationId, "1", "1")
        when: "Enter the address and save the changes"
            fillData country,fName,lName,address,city,postal,eMail
            continueButton.click()
        then: "Shipping Page is shown"
            at CheckoutShippingPage
        and: "Checkout step 2 ecommerce event for the product is trigerred"
            verifyCheckoutStepActivity(productVariationId, "1", "2", shipmentMethodStandardGroundName[0])
        when: "Save the shipment method"
            continueClick()
        then: "Payment page is shown"
            at CheckoutPaymentPage
        and: "Checkout step 3 ecommerce event for the product is trigerred"
            verifyCheckoutStepActivity(productVariationId, "1", "3")
        when: "Select the payment method and save the changes"
            cashOnDelivery()
        then: "Checkout Review Page is shown"
            at CheckoutReviewPage
        and: "Checkout step 4 ecommerce event for the product is trigerred"
            verifyCheckoutStepActivity(productVariationId, "1", "4", paymentMethodCashOnDeliveryName[0])
        when: "Submit the order"
            agreeCheckBox.click()
            submitButton.click()
        then: "Checkout Receipt page is shown"
            at CheckoutReceiptPage
        and: "purchase commerce event for the product is trigerred"
            verifyPurchaseActivity(productVariationId, "1")
        where:
            productId  << testData.get("googleTagManagerProduct.variation.sku")
            productVariationId  << testData.get("googleTagManagerProduct.variation.newsku")
            productName << testData.get("googleTagManagerProduct.variation.name")
            suggestedProductId << testData.get("lineItem.bundle.subSku3")
            variationattribute << testData.get("googleTagManagerProduct.variation.attribute")
            variationattributevalue << testData.get("googleTagManagerProduct.variation.attributevalue")
            quantity    =  testData.get("defaultPromotion.promotion.quantity")[0]
            eMail   = testData.get("RegisterIndividualCustomer.eMail")[0]
            country = testData.get("defaultUser.address.country")[0]
            fName   = testData.get("RegisterIndividualCustomer.fName")[0]
            lName   = testData.get("RegisterIndividualCustomer.lName")[0]
            address = testData.get("defaultUser.address.address1")[0]
            postal  = testData.get("defaultUser.address.zip")[0]
            city    = testData.get("defaultUser.address.city")[0]
            paymentMethodCashOnDeliveryName    = testData.get("paymentMethod.cashOnDelivery.name")
            shipmentMethodStandardGroundName    = testData.get("shippingMethod.standardGround.name")
    }

    void verifyProductDetailsActivity(productId)
    {
        log(js.dataLayer, "View of Product Details")
        def trackingData = js.dataLayer.findAll{ it.ecommerce != null}.findAll {it.ecommerce.detail != null}
        assert trackingData.event[0] == "impressionsPushed"
        assert trackingData.ecommerce.detail.products.size == 1
        def productData = trackingData.ecommerce.detail.products[0].find{it.id==productId}
        checkProductData(productData, productId)
    }

    void verifyAddToCartActivity(productId, quantity)
    {
        log(js.dataLayer, "Adding a Product to a Shopping Cart")
        def trackingData = js.dataLayer.findAll{ it.ecommerce != null}.findAll {it.ecommerce.add != null}
        assert trackingData.event[0] == "addToCart"
        assert trackingData.ecommerce.add.products.size == 1
        assert trackingData.ecommerce.currencyCode[0] == "USD"
        def productData = trackingData.ecommerce.add.products[0].find{it.id==productId}
        checkProductData(productData, productId, quantity)
    }

    void verifyRemoveFromCartActivity(productId, quantity)
    {
        log(js.dataLayer, "Removing a Product from a Shopping Cart")
        def trackingData = js.dataLayer.findAll{ it.ecommerce != null}.findAll {it.ecommerce.remove != null}
        assert trackingData.event[0] == "removeFromCart"
        assert trackingData.ecommerce.remove.products.size == 1
        assert trackingData.ecommerce.currencyCode[0] == "USD"
        def productData = trackingData.ecommerce.remove.products[0].find{it.id==productId}
        checkProductData(productData, productId, quantity)
    }

    void verifyCheckoutStepActivity(productId, quantity, stepNumber, optionValue)
    {
        log(js.dataLayer, "Checkout Step " + stepNumber)
        def trackingData = js.dataLayer.findAll{ it.ecommerce != null}.findAll {it.ecommerce.checkout != null}.findAll {it.ecommerce.checkout.products != null}
        assert trackingData.event[0] == "checkout"
        assert trackingData.ecommerce.checkout.products.size >= 1
        def productData = trackingData.ecommerce.checkout.products[0].find{it.id==productId}
        checkProductData(productData, productId, quantity)
        assert trackingData.ecommerce.checkout.actionField.step[0] == stepNumber
        if (optionValue != null)
        {
            assert trackingData.ecommerce.checkout.actionField.option[0] == optionValue
        }
    }

    void verifyPurchaseActivity(productId, quantity)
    {
        log(js.dataLayer, "Purchase")
        def trackingData = js.dataLayer.findAll{ it.ecommerce != null}.findAll {it.ecommerce.purchase != null}.findAll {it.ecommerce.purchase.products != null}
        assert trackingData.ecommerce.purchase.products.size >= 1
        def productData = trackingData.ecommerce.purchase.products[0].find{it.id==productId}
        checkProductData(productData, productId, quantity)
        assert trackingData.ecommerce.purchase.actionField.id[0] != null
        assert trackingData.ecommerce.purchase.actionField.revenue[0] != null
        assert trackingData.ecommerce.purchase.actionField.shipping[0] != null
        assert trackingData.ecommerce.purchase.actionField.tax[0] != null
    }

    void verifyCheckoutStepActivity(productId, quantity, stepNumber)
    {
        verifyCheckoutStepActivity(productId, quantity, stepNumber, null)
    }

    void verifyProductImpressionActivity(productId)
    {
        def trackingData = js.dataLayer.findAll{ it.ecommerce != null}.findAll {it.ecommerce.impressions != null}
        assert trackingData.ecommerce.impressions.size >= 1
        def productData = trackingData.ecommerce.impressions[0].find{it.id==productId}
        checkProductData(productData, productId)
    }

    void checkProductData(productData, expectedId, expectedQuantity)
    {
        assert productData != null
        assert productData.id == expectedId
        assert productData.name != null
        assert productData.price != null
        assert productData.category != null
        assert productData.brand != null
        // Not all products has variation - accept also missing variant
        if (productData.variant != null)
        {
            assert productData.variant.length() >= 1
        }
        if (expectedQuantity != null)
        {
            assert productData.quantity == expectedQuantity
        }
    }

    void checkProductData(productData, expectedId)
    {
        checkProductData(productData, expectedId, null)
    }

    void log(dataLayerObject, operation)
    {
        println "GTM dataLayer object for '" + operation + "' ecommerce activity: "
        println  new groovy.json.JsonBuilder(dataLayerObject).toPrettyString()
    }
}
