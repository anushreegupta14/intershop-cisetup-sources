package geb.com.intershop.inspired.specs.storefront.b2c.responsive

import geb.com.intershop.inspired.pages.storefront.responsive.*
import geb.com.intershop.inspired.pages.storefront.responsive.account.*
import geb.com.intershop.inspired.pages.storefront.responsive.checkout.*
import geb.com.intershop.inspired.pages.storefront.responsive.shopping.*
import geb.com.intershop.inspired.testdata.TestDataUsage
import geb.spock.GebReportingSpec
import spock.lang.*

class CheckoutAddressSpec extends GebReportingSpec implements TestDataUsage
{

    /**
     * Checkout as Registered Customer with preferred Addresses<p>
     */
	@Unroll
    def "Checkout as Registered Customer with Preferred Addresses for SKU #searchTerm"()
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

        then: "... find it at the Detail Page."
        at ProductDetailPage
        lookedForSKU searchTerm

        when: "I add it to Cart..."
        addToCart()

        then: "... and check it."
        at CartPage
        checkProduct productName

        when: "Now I will checkout..."
        checkOut()

        then: "...validate shipping and invoice address details..."
        at CheckoutPaymentPage
        orderAddressSummary.isShipToAddress(shipping_fName,shipping_lName,shipping_address,shipping_city,shipping_postal)
        orderAddressSummary.isInvoiceToAddress(fName,lName,address,city,postal)
        
        
        // for cleanup finish checkout
		when: "...choose Cash on Delivery payment method..."
        cashOnDelivery()
        
        then: "... review my Order..."
        at CheckoutReviewPage
        orderShippingAddressSlot.isShipToAddress(shipping_fName,shipping_lName,shipping_address,shipping_city,shipping_postal)
        orderInvoiceAddressSlot.isInvoiceToAddress(fName,lName,address,city,postal)
        
        when: "...and submit."
        agreeCheckBox.click()
        submitButton.click()
        
        then:
        at CheckoutReceiptPage
        orderShippingAddressSlot.isShipToAddress(shipping_fName,shipping_lName,shipping_address,shipping_city,shipping_postal)
        orderInvoiceAddressSlot.isInvoiceToAddress(fName,lName,address,city,postal)

        where:
        searchTerm  << testData.get("defaultProduct.default.sku")
        productName << testData.get("defaultProduct.default.name")
      
        user        =  testData.get("checkoutUser.login.eMail")[1]
        password    =  testData.get("checkoutUser.login.password")[0]
      
        
        eMail   = testData.get("defaultUser.login.eMail")[1]
        country = testData.get("defaultUser.address.country")[1]
        fName   = testData.get("defaultUser.address.fName")[1]
        lName   = testData.get("defaultUser.address.lName")[1]
        address = testData.get("defaultUser.address.address1")[1]
        postal  = testData.get("defaultUser.address.zip")[1]
        city    = testData.get("defaultUser.address.city")[1]
        
        shipping_country = testData.get("defaultUser.address.country")[2]
        shipping_fName   = testData.get("defaultUser.address.fName")[2]
        shipping_lName   = testData.get("defaultUser.address.lName")[2]
        shipping_address = testData.get("defaultUser.address.address1")[2]
        shipping_postal  = testData.get("defaultUser.address.zip")[2]
        shipping_city    = testData.get("defaultUser.address.city")[2]
    }

    /**
     * Checkout as Unregistered Customer with two different shipping addresses<p>
     */
    @Ignore("org.openqa.selenium.ElementClickInterceptedException")
    def "Multiple Shipment as unregistered customer"()
    {
        when: "I go to product detail page"
        to ProductDetailPage, SKU:searchTerm1

        then: "... find it at the Detail Page."
        at ProductDetailPage
        lookedForSKU searchTerm1

        when: "I add it to Cart... and checkout"
        addToCart()
        
        then: "... and check it."
        at CartPage
        checkProduct productName1

        when: "I go to product detail page"
        to ProductDetailPage, SKU:searchTerm2

        then: "... find it at the Detail Page."
        at ProductDetailPage
        lookedForSKU searchTerm2


        when: "I add it to Cart..."
        addToCart()

        then: "... and check it."
        at CartPage
        checkProduct productName2
        
        when: "Now I will checkout..."
        checkOut()

        then: "...add address,..."
        at NewUserAddressPage

        when:
        fillData country,fName,lName,address,city,postal,eMail
        shipToMultipleAddressButton.click()
        continueButton.click()

        then: "...add different addresses and assign them to product,..."
        at MultipleAddressPage
        addShippingAddress shipping_country,shipping_fName,shipping_lName,shipping_address,shipping_city,shipping_postal
        addShippingAddress shipping_country2,shipping_fName2,shipping_lName2,shipping_address2,shipping_city2,shipping_postal2
        assert (setProductAddress(productName1,shipping_fName,shipping_lName,shipping_address))
        assert (setProductAddress(productName2,shipping_fName2,shipping_lName2,shipping_address2))
       
        when:
        continueClick()

        then:
        at CheckoutShippingPage
        orderAddressSummary.isShipToAddress "Shipping to multiple addresses"
        
        // for cleanup, finish checkout
        when:
        continueClick()
        
        then:
        at CheckoutPaymentPage
		
		when:
        invoice()
        
        then: "... review my Order..."
        at CheckoutReviewPage
        
        when: "...and submit."
        agreeCheckBox.click()
        submitButton.click()
        
        then:
        at CheckoutReceiptPage
        
        where:
        
        searchTerm1  = testData.get("cartProducts.product.sku")[0]
        searchTerm2  = testData.get("cartProducts.product.sku")[1]
        productName1 = testData.get("cartProducts.product.name")[0]
        productName2 = testData.get("cartProducts.product.name")[1]
      
        eMail   = testData.get("RegisterIndividualCustomer.eMail")[0]
        country = testData.get("defaultUser.address.country")[0]
        fName   = testData.get("RegisterIndividualCustomer.fName")[0]
        lName   = testData.get("RegisterIndividualCustomer.lName")[0]
        address = testData.get("defaultUser.address.address1")[0]
        postal  = testData.get("defaultUser.address.zip")[0]
        city    = testData.get("defaultUser.address.city")[0]
         
        shipping_country = testData.get("anotherUser.address.country")[0]
        shipping_fName   = testData.get("anotherUser.address.fName")[0]
        shipping_lName   = testData.get("anotherUser.address.lName")[0]
        shipping_address = testData.get("anotherUser.address.address1")[0]
        shipping_postal  = testData.get("anotherUser.address.zip")[0]
        shipping_city    = testData.get("anotherUser.address.city")[0]
        
        shipping_country2 = testData.get("anotherUser.address.country")[1]
        shipping_fName2   = testData.get("anotherUser.address.fName")[1]
        shipping_lName2   = testData.get("anotherUser.address.lName")[1]
        shipping_address2 = testData.get("anotherUser.address.address1")[1]
        shipping_postal2  = testData.get("anotherUser.address.zip")[1]
        shipping_city2    = testData.get("anotherUser.address.city")[1]
    }
    
    
     /**
     * Checkout as Unregistered Customer with two different addresses<p>
     */
   
    def "Seperate ShipTo and InvoiceTo Address as unregistered customer"()
    {
        when: "I go to product detail page"
        	to ProductDetailPage, SKU:searchTerm

        then: "... find it at the Detail Page."
            at ProductDetailPage
            lookedForSKU searchTerm

        when: "I add it to Cart... and checkout"
            addToCart()
        
        then: "... and check it."
            at CartPage
            checkProduct productName

        when: "Now I will checkout..."
            checkOut()

        then: "...add address,..."
            at NewUserAddressPage

        when:
            fillData country,fName,lName,address,city,postal,eMail
            shipToDifferentAddressButton.click()
            sleepForNSeconds(20)
            fillShippingData shipping_country,shipping_fName,shipping_lName,shipping_address,shipping_city,shipping_postal
            continueButton.click()

        then: "...choose shipping method,..."
            at CheckoutShippingPage
            orderAddressSummary.isInvoiceToAddress(fName,lName,address,city,postal)
            orderAddressSummary.isShipToAddress(shipping_fName,shipping_lName,shipping_address,shipping_city,shipping_postal)
        
        // for cleanup, finish checkout
        when:
            continueClick()

        then:
            at CheckoutPaymentPage
		
		when:
            cashOnDelivery()
        
        then: "... review my Order..."
            at CheckoutReviewPage
        
        when: "...and submit."
            agreeCheckBox.click()
            submitButton.click()
        
        then:
            at CheckoutReceiptPage
        
        where:
            searchTerm  << testData.get("defaultProduct.default.sku")
            productName << testData.get("defaultProduct.default.name")
          
            eMail   = testData.get("RegisterIndividualCustomer.eMail")[0]
            country = testData.get("defaultUser.address.country")[0]
            fName   = testData.get("RegisterIndividualCustomer.fName")[0]
            lName   = testData.get("RegisterIndividualCustomer.lName")[0]
            address = testData.get("defaultUser.address.address1")[0]
            postal  = testData.get("defaultUser.address.zip")[0]
            city    = testData.get("defaultUser.address.city")[0]
             
            shipping_country = testData.get("anotherUser.address.country")[0]
            shipping_fName   = testData.get("anotherUser.address.fName")[0]
            shipping_lName   = testData.get("anotherUser.address.lName")[0]
            shipping_address = testData.get("anotherUser.address.address1")[0]
            shipping_postal  = testData.get("anotherUser.address.zip")[0]
            shipping_city    = testData.get("anotherUser.address.city")[0]
    }
    
    /**
     * Change addresses during the checkout process<p>
     */
    @Ignore("org.openqa.selenium.ElementClickInterceptedException")
    def "Select Address during checkout as unregistered customer"()
    {
        when: "I go to product detail page"
        	to ProductDetailPage, SKU:searchTerm

        then: "... find it at the Detail Page."
            at ProductDetailPage
            lookedForSKU searchTerm

        when: "I add it to Cart... and checkout"
            addToCart()
        
        then: "... and check it."
            at CartPage
            checkProduct productName

        when: "Now I will checkout..."
            checkOut()

        then: "...on checkout address page..."
            at NewUserAddressPage

        when: "...add addresses,..."
            fillData country,fName,lName,address,city,postal,eMail
            shipToDifferentAddressButton.click()
            sleepForNSeconds(20)
            fillShippingData shipping_country,shipping_fName,shipping_lName,shipping_address,shipping_city,shipping_postal
            continueClick()            
            
        then: "...on checkout shipping page ..."
            at CheckoutShippingPage
            
        when: "...go back to address page and select shipping address as invoice address..."
            navigationBar.gotoCheckoutAddressPage()
                        
        then: "...on checkout address page..."
            at CheckoutAddressPage
                        
        when: "...address should selected..."
            String name = shipping_fName+ " " + shipping_lName +", "+ shipping_address+ ", " + shipping_city
            selectInvoiceAddress(name)
            
        then: "...is displayed as invoice address..."
            invoiceAddressText.contains(shipping_fName+ " " + shipping_lName)
            invoiceAddressText.contains(shipping_address)
            invoiceAddressText.contains(shipping_city)
            invoiceAddressText.contains(shipping_postal)
        
        // for cleanup, finish checkout
        when: "...continue checkout..."
            continueClick()

        then: "...on checkout shipping page..."
            at CheckoutShippingPage
            
        when: "...continue checkout..."
            continueClick()

        then: "...on checkout payment page..."
            at CheckoutPaymentPage
        
        when: "...select payment method..."
            cashOnDelivery()
        
        then: "... review my Order..."
            at CheckoutReviewPage
        
        when: "...and submit."
            agreeCheckBox.click()
            submitButton.click()
        
        then:
            at CheckoutReceiptPage
        
        where:
            searchTerm  << testData.get("defaultProduct.default.sku")
            productName << testData.get("defaultProduct.default.name")
          
            eMail   = testData.get("RegisterIndividualCustomer.eMail")[0]
            country = testData.get("defaultUser.address.country")[0]
            fName   = testData.get("RegisterIndividualCustomer.fName")[0]
            lName   = testData.get("RegisterIndividualCustomer.lName")[0]
            address = testData.get("defaultUser.address.address1")[0]
            postal  = testData.get("defaultUser.address.zip")[0]
            city    = testData.get("defaultUser.address.city")[0]
             
            shipping_country = testData.get("anotherUser.address.country")[0]
            shipping_fName   = testData.get("anotherUser.address.fName")[0]
            shipping_lName   = testData.get("anotherUser.address.lName")[0]
            shipping_address = testData.get("anotherUser.address.address1")[0]
            shipping_postal  = testData.get("anotherUser.address.zip")[0]
            shipping_city    = testData.get("anotherUser.address.city")[0]
    }
    
}
