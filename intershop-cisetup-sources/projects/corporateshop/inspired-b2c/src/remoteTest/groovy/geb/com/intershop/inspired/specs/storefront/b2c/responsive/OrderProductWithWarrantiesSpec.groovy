package geb.com.intershop.inspired.specs.storefront.b2c.responsive

import geb.com.intershop.inspired.pages.storefront.responsive.*
import geb.com.intershop.inspired.pages.storefront.responsive.account.*;
import geb.com.intershop.inspired.pages.storefront.responsive.checkout.warranty.*;
import geb.com.intershop.inspired.pages.storefront.responsive.shopping.*;
import geb.com.intershop.inspired.pages.storefront.responsive.shopping.warranty.*;
import geb.com.intershop.inspired.testdata.TestDataUsage
import geb.spock.GebReportingSpec
import spock.lang.*;


/**
 * Storefront test for Product with Warranties and Warranty Products at inSPIRED
 */
class OrderProductWithWarrantiesSpec extends GebReportingSpec implements TestDataUsage
{

    /**
     * Add a Product with Warranties Cart<p>
     */
    def "Add a Product with Warranties to Cart"()
    {
        when: "I go to the signIn page and log in..."
        to HomePage
        at HomePage
        pressLogIn()

        then:
        at AccountLoginPage

        when: " Login registered user ..."
        login user,password

        then: "... then I'm logged in."
        at AccountPage
        
        when: "I go to product detail page"
        to ProductDetailPage, SKU:productSku

        then: "Then I find it at the Detail Page."
        at ProductWithWarrantyDetailPage
        lookedForSKU(productSku)
        and: "... check if warranty product(s) displayed on Detail Page."
        checkWarrantyProductExists(warrantyProductSku)

        when: "I add it to Cart..."
        addToCart()

        then: "... and check all values I'm interested in."
        at CartWithWarrantyPage
        checkProduct(productName)
        
        when: "... select one warranty product in DropDown Box"
        selectWarrantyProductInDropDown(warrantyProductSku)
        
        then: "... Cart Page got reloaded after warranty product selection ..."
        at CartWithWarrantyPage
        checkProduct(productName)
        
        when: "Now I will checkout..."
        checkOut()

        then: "...choose Payment if asked..."
        at CheckoutPaymentWithWarrantyPage
        // check if warranty product name is displayed
        checkLineItemWithWarranty(warrantyProductName)
        when: " ... select cashOnDelivery and click submit button ..."
        cashOnDelivery()
        
        then: "... review my Order... and verify CheckoutReviewPage displays warranty product"
        at CheckoutReviewWithWarrantyPage
        checkLineItemWithWarranty(warrantyProductName)
        
        when: "...and submit."
        agreeCheckBox.click()
        submitButton.click()

        then: "... check warranty product exists in Checkout Receipt Page"
        at CheckoutReceiptWithWarrantyPage
        checkLineItemWithWarranty(warrantyProductName)
        
        when: "I go to the HomePage page"
        to HomePage
        
        then: "...and log out..."
        at HomePage
        pressLogOut()

        where:
        productSku  << testData.get("firstProduct.productWithWarranties.sku")
        productName << testData.get("firstProduct.productWithWarranties.name")
        warrantyProductSku  << testData.get("secondProduct.warranty.sku")
        warrantyProductName << testData.get("secondProduct.warranty.name")
        user        =  testData.get("checkoutUser.login.eMail")[0]
        password    =  testData.get("checkoutUser.login.password")[0]
    }

    /**
     * Add a Product and one of its Warranties to Cart<p>
     */
    def "Add a Product and one of its Warranties to Cart"()
    {
        when: "I go to the signIn page and log in..."
        to HomePage
        at HomePage
        pressLogIn()

        then:
        at AccountLoginPage

        when: " Login registered user ..."
        login user,password

        then: "... then I'm logged in."
        at AccountPage
        
        when: "I go to product detail page"
        to ProductDetailPage, SKU:productSku

        then: "Then I find it at the Detail Page."
        at ProductWithWarrantyDetailPage
        lookedForSKU(productSku)
        
        when: "... check if warranty product(s) displayed on Detail Page."
        checkWarrantyProductExists(warrantyProductSku)
        then: "... select warranty product ..."
        selectWarrantyProduct(warrantyProductSku)
        
        when: "I add it to Cart..."
        addToCart()

        then: "... and check all values I'm interested in."
        at CartWithWarrantyPage
        checkProduct(productName)
        
        when: "Now I will checkout..."
        checkOut()

        then: "...choose Payment if asked..."
        at CheckoutPaymentWithWarrantyPage
        // check is waranty product name is displayed
        checkLineItemWithWarranty(warrantyProductName)
        when: " ... select cashOnDelivery and click submit button ..."
        cashOnDelivery()
        
        then: "... review my Order... and verify CheckoutReviewPage displays warranty product"
        at CheckoutReviewWithWarrantyPage
        checkLineItemWithWarranty(warrantyProductName)
        
        when: "...and submit."
        agreeCheckBox.click()
        submitButton.click()

        then: "... check warranty product exists in Checkout Receipt Page"
        at CheckoutReceiptWithWarrantyPage
        checkLineItemWithWarranty(warrantyProductName)
        
        when: "I go to the HomePage page"
        to HomePage
        
        then: "...and log out..."
        at HomePage
        pressLogOut()

        where:
        productSku  << testData.get("firstProduct.productWithWarranties.sku")
        productName << testData.get("firstProduct.productWithWarranties.name")
        warrantyProductSku  << testData.get("secondProduct.warranty.sku")
        warrantyProductName << testData.get("secondProduct.warranty.name")
        user        =  testData.get("checkoutUser.login.eMail")[0]
        password    =  testData.get("checkoutUser.login.password")[0]
    }
}
