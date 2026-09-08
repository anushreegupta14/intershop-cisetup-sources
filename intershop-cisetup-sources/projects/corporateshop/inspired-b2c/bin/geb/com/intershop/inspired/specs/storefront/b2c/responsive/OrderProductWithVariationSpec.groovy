package geb.com.intershop.inspired.specs.storefront.b2c.responsive

import geb.com.intershop.inspired.pages.storefront.responsive.*
import geb.com.intershop.inspired.pages.storefront.responsive.account.*;
import geb.com.intershop.inspired.pages.storefront.responsive.checkout.CartPage
import geb.com.intershop.inspired.pages.storefront.responsive.checkout.variation.*;
import geb.com.intershop.inspired.pages.storefront.responsive.shopping.*;
import geb.com.intershop.inspired.testdata.TestDataUsage
import geb.spock.GebReportingSpec
import spock.lang.*;

/**
 * Storefront test for Product with Variations at inSPIRED
 */
class OrderProductWithVariationSpec extends GebReportingSpec implements TestDataUsage
{
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
     * Edit Variation Product in Cart<p>
     */
    @Ignore
    def "Edit Variation Product in Cart"()
    {
        when: "I go to product detail page"
        to ProductDetailPage, SKU:searchTerm

        then: "... find it at the Detail Page."
        at ProductDetailPage
        lookedForSKU searchTerm

        when: "I add it to Cart..."
        addToCart()

        then: "... and check it."
        at CartWithVariationPage
        checkProduct productName
        existingPLIs.push productName
        ! isVariationSelected(variationattributevalue)
        
        when: "I open the edit variation dialog ..."
        editVariationLink.click()
        
        then: "... and change some attributes."
        at EditVariationModal
        productVariationSelect(variationattribute).variationSelection.findAll({ it.displayed }).value(variationattributevalue)       
        // after changing selection, window reloads --> test needs to wait for that
        sleepForNSeconds(3)
        quantityInput.value quantity
        
        when: "I save the changes... "
        saveVariationButton.click()
        
        then: "... and the new values are taken."
        at CartWithVariationPage
        productCartTable(productName).quantityInput.value() == (quantity+"")
        isVariationSelected variationattributevalue

        where:
        searchTerm  << testData.get("defaultProduct.variation.sku")
        productName << testData.get("defaultProduct.variation.name")
        variationattribute << testData.get("defaultProduct.variation.attribute")
        variationattributevalue << testData.get("defaultProduct.variation.attributevalue")
        quantity    =  testData.get("defaultPromotion.promotion.quantity")[0]
        
   
    }    
    
    /**
     * Add Variation Product to Cart<p>
     *
     * Old Smoke Test:
     * testAddVariationProductToCart(...)
     */
    def "Add Variation Product to Cart"()
    {
        when: "I go to product detail page"
        to ProductDetailPage, SKU:searchTerm

        then: "... find it at the Detail Page."
        at ProductDetailPage
        lookedForSKU searchTerm

        when: "I add it to Cart..."
        addToCart()

        then: "... and check it."
        at CartWithVariationPage
        checkProduct productName
        existingPLIs.push productName

        where:
        searchTerm  << testData.get("defaultProduct.variation.sku")
        productName << testData.get("defaultProduct.variation.name")
   
    }
}
