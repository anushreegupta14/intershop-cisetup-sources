package geb.com.intershop.inspired.specs.storefront.b2c.responsive

import geb.com.intershop.inspired.pages.storefront.responsive.*
import geb.com.intershop.inspired.pages.storefront.responsive.account.*;
import geb.com.intershop.inspired.pages.storefront.responsive.checkout.*;
import geb.com.intershop.inspired.pages.storefront.responsive.shopping.*;
import geb.com.intershop.inspired.testdata.TestDataUsage
import geb.spock.GebReportingSpec
import spock.lang.*;


/**
 * Storefront tests for Merchandizing at inSPIRED,
 * based on old Smoke Tests.
 * @author Sebastian Glass
 *
 */
class MerchandizingSpec extends GebReportingSpec implements TestDataUsage
{
    private static cardNumbers = new Stack()
    private static cardPINs = new Stack()
    // Stack for Test: "existing Lineitems"
    private static existingPLIs = new Stack()

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
    }

    /**
     * Compare Products<p>
     *
     * Old Smoke Test<br> testCompareProducts(...)
     * @return
     */
    def "Compare Products"()
    {
        when: "I go to the home page and search "+searchTerm+"."
        to HomePage
        at HomePage
        header.search searchTerm

        then: "Now I'm at the Search Result Page..."
        at SearchResultPage

        when: "...with product."
        def productTile = productTiles(searchTerm)

        then: "There is a ExpressLink and..."
        productTile.expressShopContainerDiv.size()>0

        when: "...I click it..."
        productTile.clickExpressShop()

        then: "... see ExpressView..."
        at ExpressShopModal

        when: "... and add to compare."
        compareButton(sku).click()

        then:"Now there is 1 Product to compare with."
        at ComparePage

        when: "I search the second product..."
        header.search searchTerm2

        then: "...and find it at the Search Result Page!"
        at SearchResultPage

        when: "Mark it as correct..."
        productTile = productTiles(searchTerm2)

        then: "...find ExpressLink..."
        productTile.expressShopContainerDiv.size()>0

        when: "and click it..."
        productTile.clickExpressShop()

        then: "... see ExpresView..."
        at ExpressShopModal

        when: "... and add to Compare."
        compareButton(sku2).click()

        then:"Now there are 2 products to Compare."
        at ComparePage
        productField(searchTerm).size()>0
        productField(searchTerm2).size()>0

        where:
        searchTerm  = testData.get("defaultProduct.default.name")[0]
        sku         = testData.get("defaultProduct.default.sku")[0]
        searchTerm2 = testData.get("defaultProduct.default.name")[1]
        sku2        = testData.get("defaultProduct.default.sku")[1]

    }

    def "Check Promotion"()
    {
        when: "I go to product detail page"
        to ProductDetailPage, SKU:productSKU

        then: "check product detail page."
        at ProductDetailPage
        promotionList.size()>0

        where:
        productSKU <<  testData.get("defaultProduct.promotion.sku")
    }

    def "Use Promotion Code"()
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
        quantityInput.value count
        addToCart()

        then: "... and check it."
        at CartPage
        checkProduct productName
        existingPLIs.push productName

        when: "Now I will checkout..."
        checkOut()

        and: "...and be at Payment page."
        at CheckoutPaymentPage

        and: "I add my promoCode and check for response..."
        addPromoCode(promoCode)

        and:"...choose Pay on Delivery..."
        cashOnDelivery()

        then: "... review my Order..."
        at CheckoutReviewPage

        when: "...and submit."
        agreeCheckBox.click()
        submitButton.click()

        then:
        at CheckoutReceiptPage
        existingPLIs.pop()

        where:
        productName = testData.get("defaultProduct.default.name")[0]
        searchTerm  = testData.get("defaultProduct.default.sku")[0]
        count       = testData.get("defaultPromotion.promotion.quantity")[0]
        promoCode   = testData.get("defaultPromotion.promotion.code")[0]
        user        = testData.get("checkoutUser.login.eMail")[0]
        password    = testData.get("checkoutUser.login.password")[0]

    }

    def "Buy a Gift Card"(){
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
        to ProductDetailPage, SKU:productSKU

        then: "check product detail page."
        at ProductDetailPage

        when:
        addToCart()

        then: "... and check it."
        at CartPage
        checkProduct productName
        existingPLIs.push productName

        when: "Now I will checkout..."
        checkOut()

        and: "...and be at Payment page."
        at CheckoutPaymentPage
        cashOnDelivery()

        then: "... review my Order..."
        at CheckoutReviewPage

        when: "...and submit."
        agreeCheckBox.click()
        submitButton.click()

        then:
        at CheckoutReceiptPage
        def giftcardNumber = $("div[data-testing-id='giftcardNumber'] span")
        println(giftcardNumber.text())
        cardNumbers.push giftcardNumber.text()
        def giftcardPin = $("p[data-testing-id='giftcardPin'] span")
        cardPINs.push giftcardPin.text()
        println(giftcardPin.text())
        existingPLIs.pop()

        where:
        productName << testData.get("defaultPromotion.gift.card")
        productSKU  << testData.get("defaultPromotion.gift.card.sku")
        user        = testData.get("checkoutUser.login.eMail")[0]
        password    = testData.get("checkoutUser.login.password")[0]
        user2       = testData.get("defaultUser.login.eMail")[0]
        password2   = testData.get("defaultUser.login.password")[0]
    }

    @Ignore ("Problem with Gecko driver")
    def "Check Gift Card Value"() {
        when:
        to HomePage
        at HomePage

        and:
        pressLogIn()

        then:
        at AccountLoginPage

        when:
        login user,password

        then:
        at AccountPage
        giftCardBalance.click()

        then:
        at GiftCardBalanceLookupPage

        when:
        cardNumberInput.value cardNumbers.pop()
        cardPINInput.value cardPINs.pop()
        checkButton.click()

        then:""
        waitFor{$("div",text:iContains("Gift Card")).size()>0}
        $("button",name:"checkCard")

        where:
        user       = testData.get("defaultUser.login.eMail")[0]
        password   = testData.get("defaultUser.login.password")[0]

    }

	@Ignore
    def "Buy a Gift Certificate"(){
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
        to ProductDetailPage, SKU:productSKU

        then: "check product detail page."
        at ProductDetailPage

        when:

        $("div",class:"add-to-cart",0).$("a").click()

        then:""
        sleepForNSeconds(2)
        $("input",id:"GiftCertificateForm_RecipientEmail").value user2
        $("textarea",id:"GiftCertificateForm_Greeting").value "Hello :3"
        $("button",name:"addProduct").click()

        then: "... and check it."
        at CartPage
        checkProduct productSKU
        existingPLIs.push productSKU

        when: "Now I will checkout..."
        checkOut()

        and: "...and be at Payment page."
        at CheckoutPaymentPage
        ishDemoOnlinePay()

        then: "... review my Order..."
        at CheckoutReviewPage

        when: "...and submit."
        agreeCheckBox.click()
        submitButton.click()

        then:"Im at ISH PAY Site with all my entered data."
        waitFor{$("div",class:"ipay_header").size()>0}

        when:"All I have to do is clock ok."
        $('button',type:'submit').click();

        then:"My TAN I also preentered"
        waitFor{$("div",class:"ipay-onlinePayPinTan").size()>0}

        when:"All I have to do is clock ok."
        $('button',type:'submit').click();

        then:"Now I paid."
        waitFor{$("h3",text:iContains("Payment Successful")).size()>0}

        when:
        $('button',name:'back').click();

        then:
        at CheckoutReceiptPage
        existingPLIs.pop()

        where:
        productSKU << testData.get("defaultPromotion.gift.certificate.sku")
        user        = testData.get("checkoutUser.login.eMail")[0]
        password    = testData.get("checkoutUser.login.password")[0]
        user2       = testData.get("defaultUser.login.eMail")[0]
        password2   = testData.get("defaultUser.login.password")[0]

    }
}
