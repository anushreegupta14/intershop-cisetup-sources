package geb.com.intershop.inspired.specs.storefront.b2c.responsive

import geb.com.intershop.inspired.pages.storefront.responsive.*
import geb.com.intershop.inspired.pages.storefront.responsive.account.*
import geb.com.intershop.inspired.pages.storefront.responsive.checkout.*
import geb.com.intershop.inspired.pages.storefront.responsive.shopping.*
import geb.com.intershop.inspired.testdata.TestDataUsage
import geb.spock.GebReportingSpec
import spock.lang.*

/**
 * Storefront tests for Payment API at inSPIRED
 * @author skoch
 *
 */
class PaymentSpec extends GebReportingSpec implements TestDataUsage
{
    // Stack for Test: "existing Lineitems"
    private static existingPLIs = new Stack();

    // improve clean up (IS-21875) and ensure user is logged out
    def cleanup()
    {
        def zeroQuantity = "0"
        if(!existingPLIs.empty)
        {
            when: "I go to the homepage..."
                to HomePage
                at HomePage
            
            then: "... open basket ..."
                header.showMiniCart()
                header.viewCartMiniCart()
                
            and: "... and remove all line items"
                at CartPage
                while ( !existingPLIs.empty )
                {
                    def name = existingPLIs.pop()
                    productCartTable(name).removeLink.click()
                }
        }
        
        when: "I go back to the homepage..."
            to HomePage
            at HomePage
            
        then: 
            if ( isSignedIn(true) )
            {
                pressLogOut()
            }
    }
    
    
    /**
     * Execute checkout with redirect before payment method
     *
     */
    def "Redirect before payment checkout"()
    {
        when: "I go to the signIn page and log in..."
        to HomePage
        at HomePage
        pressLogIn()

        then:
        at AccountLoginPage

        when:
        login user,password

        then: "... then I'm logged in."
        at AccountPage

        when: "I go to product detail page"
        to ProductDetailPage, SKU:searchTerm

        then: "check product detail page."
        at ProductDetailPage
        lookedForSKU searchTerm

        when: "I add it to Cart..."
        addToCart()

        then: "... and check it."
        at CartPage
        checkProduct productName
        existingPLIs.push productName

        when: "Now I will checkout..."
        checkOut()

        and: "...choose Payment if asked..."
        at CheckoutPaymentPage
        creditCard cardNumber,expDate,type
        
        then: "... redirect to payment provider ..."
        at DummyPaymentProviderPage
        submitRedirectBeforeRequest()
        
        when: "... review my Order..."
        at CheckoutReviewPage

        then: "...and submit."
        agreeCheckBox.click()
        submitButton.click()
        
        and:"... receipt page is displayed ..."
        at CheckoutReceiptPage
        existingPLIs.pop()

        where:
        searchTerm  = testData.get("defaultProduct.default.sku")[0];
        productName = testData.get("defaultProduct.default.name")[0];
      
        user        =  testData.get("paymentMethod.user.login.eMail")[0];
        password    =  testData.get("paymentMethod.user.login.password")[0];
        
        cardNumber = testData.get("paymentMethod.creditCard.number")[0];
        expDate = testData.get("paymentMethod.creditCard.expDate")[0];
        type = testData.get("paymentMethod.creditCard.type")[0];
    }

    /**
     * Execute checkout with fallback redirect before payment method. In case
     * the user was already redirected, but e. g. decides to go back and add
     * some more items to the basket, he will be redirected again at the review
     * page. 
     *
     */
    def "Redirect before payment checkout fallback"()
    {
        when: "I go to the signIn page and log in..."
        to HomePage
        at HomePage
        pressLogIn()

        then:
        at AccountLoginPage

        when:
        login user,password

        then: "... then I'm logged in."
        at AccountPage

        when: "I go to product detail page"
        to ProductDetailPage, SKU:searchTerm

        then: "check product detail page."
        at ProductDetailPage
        lookedForSKU searchTerm

        when: "I add it to Cart..."
        addToCart()

        then: "... and check it."
        at CartPage
        checkProduct productName
        existingPLIs.push productName

        when: "Now I will checkout..."
        checkOut()

        and: "...choose Payment if asked..."
        at CheckoutPaymentPage
        creditCard cardNumber,expDate,type
        
        then: "... redirect to payment provider ..."
        at DummyPaymentProviderPage
        submitRedirectBeforeRequest()
        
        and: "... review my Order..."
        at CheckoutReviewPage
        existingPLIs.pop()

        when: "I go to product detail page"
        to ProductDetailPage, SKU:searchTerm2

        then: "check product detail page."
        at ProductDetailPage
        lookedForSKU searchTerm2

        when: "I add it to Cart..."
        addToCart()

        then: "... and check it."
        at CartPage
        checkProduct productName2
        existingPLIs.push productName2

        when: "Now I will checkout..."
        checkOut()

        then: "...submit on the Payment page..."
        at CheckoutPaymentPage
        continueButton.click()

        when: "I am being redirected to payment provider again ..."
        at DummyPaymentProviderPage
        submitRedirectBeforeRequest()
        
        then: "... review my Order..."
        at CheckoutReviewPage

        and: "...and submit."
        agreeCheckBox.click()
        submitButton.click()

        when: "Order is created ..."
        at CheckoutReceiptPage
        
        then:"... and finally removal of pli's from the stack."
        existingPLIs.pop()

        where:
        searchTerm  = testData.get("defaultProduct.default.sku")[0];
        productName = testData.get("defaultProduct.default.name")[0];
        searchTerm2 = testData.get("defaultProduct.default.sku")[1];
        productName2 = testData.get("defaultProduct.default.name")[1];
      
        user        =  testData.get("paymentMethod.user.login.eMail")[1]
        password    =  testData.get("paymentMethod.user.login.password")[0]
        
        cardNumber = testData.get("paymentMethod.creditCard.number")[0];
        expDate = testData.get("paymentMethod.creditCard.expDate")[0];
        type = testData.get("paymentMethod.creditCard.type")[0];
    }
    
    /**
     * Execute checkout with redirect before payment method which fails
     */
    def "Redirect before payment checkout - Failure"()
    {
        when: "I go to the signIn page and log in..."
        to HomePage
        at HomePage
        pressLogIn()

        then:
        at AccountLoginPage

        when:
        login user,password

        then: "... then I'm logged in."
        at AccountPage

        when: "I go to product detail page"
        to ProductDetailPage, SKU:searchTerm

        then: "check product detail page."
        at ProductDetailPage
        lookedForSKU searchTerm

        when: "I add it to Cart..."
        addToCart()

        then: "... and check it."
        at CartPage
        checkProduct productName
        existingPLIs.push productName

        when: "Now I will checkout..."
        checkOut()

        then: "...choose Payment if asked..."
        at CheckoutPaymentPage
        creditCard cardNumberFailure,expDate,type
        
        when: "... redirect to payment provider ..."
        at DummyPaymentProviderPage
        submitRedirectBeforeRequest()
        
        then: "... review my Order..."
        at CheckoutReviewPage

        when: "...and submit."
        agreeCheckBox.click()
        submitButton.click()
        
        and: "... payment fails and the user lands on payment page again ..."
        at CheckoutPaymentPage
        error // check if an error is shown
        cashOnDelivery() // continue with other payment method. This ensures also that basket is "left in clean" state.
        
        then: "... review my Order again..."
        at CheckoutReviewPage

        when: "...and submit."
        agreeCheckBox.click()
        submitButton.click()

        then:
        at CheckoutReceiptPage
        existingPLIs.pop()

        where:
        searchTerm  = testData.get("defaultProduct.default.sku")[0];
        productName = testData.get("defaultProduct.default.name")[0];
              
        user        =  testData.get("paymentMethod.user.login.eMail")[2];
        password    =  testData.get("checkoutUser.login.password")[0];
        
        cardNumberFailure = testData.get("paymentMethod.creditCardFailure.number")[0];
        expDate = testData.get("paymentMethod.creditCard.expDate")[0];
        type = testData.get("paymentMethod.creditCard.type")[0];
    }
    
    /**
     * Execute checkout with redirect after payment method
     *
     */
    def "Redirect after payment checkout"()
    {
        when: "I go to the signIn page and log in..."
        to HomePage
        at HomePage
        pressLogIn()

        then:
        at AccountLoginPage

        when:
        login user,password

        then: "... then I'm logged in."
        at AccountPage

        when: "I go to product detail page"
        to ProductDetailPage, SKU:searchTerm

        then: "check product detail page."
        at ProductDetailPage
        lookedForSKU searchTerm

        when: "I add it to Cart..."
        addToCart()

        then: "... and check it."
        at CartPage
        checkProduct productName
        existingPLIs.push productName

        when: "Now I will checkout..."
        checkOut()

        and: "...choose Payment if asked..."
        at CheckoutPaymentPage
        ishDemoOnlinePay()
        
        then: "... review my Order..."
        at CheckoutReviewPage

        when: "...and submit."
        agreeCheckBox.click()
        submitButton.click()
        
        then:"... redirect to payment provider ..."
        at DummyPaymentProviderPage
        
        when:"... submit on provider page ..."
        submitRedirectAfterRequest();
        
        then:"... receipt page is displayed ..."
        at CheckoutReceiptPage
        existingPLIs.pop()

        where:
        searchTerm  = testData.get("defaultProduct.default.sku")[0]
        productName = testData.get("defaultProduct.default.name")[0]
      
        user        =  testData.get("paymentMethod.user.login.eMail")[3]
        password    =  testData.get("paymentMethod.user.login.password")[0]
    }
    
    /**
     * Execute checkout with redirect by canceled payment
     *
     */
    def "Redirect by canceled payment checkout"()
    {
        when: "I go to the signIn page and log in..."
        to HomePage
        at HomePage
        pressLogIn()

        then:
        at AccountLoginPage

        when:
        login user,password

        then: "... then I'm logged in."
        at AccountPage   

        when: "I go to product detail page"
        to ProductDetailPage, SKU:searchTerm

        then: "check product detail page."
        at ProductDetailPage
        lookedForSKU searchTerm

        when: "I add it to Cart..."
        addToCart()

        then: "... and check it."
        at CartPage
        checkProduct productName
        existingPLIs.push productName

        when: "Now I will checkout..."
        checkOut()

        and: "...choose Payment if asked..."
        at CheckoutPaymentPage
        ishDemoOnlinePay()
        
        then: "... review my Order..."
        at CheckoutReviewPage

        when: "...and submit."
        agreeCheckBox.click()
        submitButton.click()
        
        then:"... redirect to payment provider ..."
        at DummyPaymentProviderPage
        
        when:"... canceled payment on provider page ..."
        cancelRequest();
        
        and: "... redirect to payment page ..."
        at CheckoutPaymentPage
        
        then:"... account order history page is displayed ..."
        to AccountOrderHistoryPage
        at AccountOrderHistoryPage
        
        checkOrderHistoryLine("payment canceled")
       
/*
        when:"... go to cart page ..."
        to CartPage
        then : "... on the cart page ..."
        at CartPage
        
        when:"... remove payment canceled product from cart ..."
        removeProductFromCart();
        
        then : "... go to empty cart page"
        at EmptyCartPage
*/
                
        where:
        searchTerm  = testData.get("defaultProduct.default.sku")[0]
        productName = testData.get("defaultProduct.default.name")[0]
      
        user        =  testData.get("paymentMethod.user.login.eMail")[4]
        password    =  testData.get("paymentMethod.user.login.password")[0]
    }

    /**
     * Execute checkout with fast pay payment method
     *
     */
    def "Fast pay checkout"()
    {
        when: "I go to the signIn page and log in..."
        to HomePage
        at HomePage
        pressLogIn()

        then:
        at AccountLoginPage

        when:
        login user,password

        then: "... then I'm logged in."
        at AccountPage

        when: "I go to product detail page"
        to ProductDetailPage, SKU:searchTerm

        then: "check product detail page."
        at ProductDetailPage
        lookedForSKU searchTerm

        when: "I add it to Cart..."
        addToCart()

        then: "... and check it."
        at CartPage
        existingPLIs.push productName
        checkOutFastPay()

        then: "... redirect to payment provider ..."
        at DummyPaymentProviderPage
        submitFastPayRequest()

        then: "... review my Order..."
        at CheckoutReviewPage
        existingPLIs.pop()

        where:
        searchTerm  = testData.get("defaultProduct.default.sku")[0]
        productName = testData.get("defaultProduct.default.name")[0]

        user        =  testData.get("paymentMethod.user.login.eMail")[5]
        password    =  testData.get("paymentMethod.user.login.password")[0]
    }
}
