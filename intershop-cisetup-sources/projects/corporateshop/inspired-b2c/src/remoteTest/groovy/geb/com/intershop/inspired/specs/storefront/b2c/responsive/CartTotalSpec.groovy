package geb.com.intershop.inspired.specs.storefront.b2c.responsive

import geb.com.intershop.inspired.pages.backoffice.*
import geb.com.intershop.inspired.pages.storefront.responsive.*
import geb.com.intershop.inspired.pages.storefront.responsive.account.*
import geb.com.intershop.inspired.pages.storefront.responsive.checkout.*
import geb.com.intershop.inspired.pages.storefront.responsive.shopping.*
import geb.com.intershop.inspired.testdata.TestDataUsage
import geb.spock.GebReportingSpec
import spock.lang.*

/**
 * Storefront test for Min Cart Total Value at inSPIRED
 */
class CartTotalSpec extends GebReportingSpec implements TestDataUsage
{
    
    def setupSpec()
    {
        
        when: "I go to the backoffice..."
        to BOLoginPage
        at BOLoginPage

        then: "...and set min item totals."
        navigateToB2CCartSettings()
        setBasketMinTotalValue("200")
        
    }

	def cleanup()
    {
        when: "I go to the backoffice..."
        to BOLoginPage
        at BOLoginPage

        then: "...and set min item totals."
        navigateToB2CCartSettings()
        setBasketMinTotalValue("0")
    }

    /**
     * Add Bundled Product to Cart<p>
     *
     * Old Smoke Test:
     * testAddBundledProductToCart(...)
     */
    def "Add Bundled Product to Cart"()
    {
        when: "I go to product detail page"
        to ProductDetailPage, SKU:searchTerm

        then: "Detail Page."
        at ProductDetailPage
        lookedForSKU searchTerm

        when: "I add it to Cart..."
        addToCart()

        then: "... checkout ..."
        at CartPage
        checkOut()

        then: "... check the error message is shown."
        waitFor{$("li",text:iContains(errorMsg)).size()>0}
        at CartPage

        where:
        searchTerm  << testData.get("defaultProduct.variation.sku")
        productName << testData.get("defaultProduct.variation.name")
        errorMsg << testData.get("cartTotal.error")
        
    }
}
