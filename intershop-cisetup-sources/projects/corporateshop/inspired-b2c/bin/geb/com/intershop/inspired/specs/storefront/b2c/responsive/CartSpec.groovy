package geb.com.intershop.inspired.specs.storefront.b2c.responsive

import geb.com.intershop.inspired.pages.storefront.responsive.*
import geb.com.intershop.inspired.pages.storefront.responsive.checkout.*;
import geb.com.intershop.inspired.pages.storefront.responsive.shopping.*;
import geb.com.intershop.inspired.testdata.TestDataUsage
import geb.spock.GebReportingSpec

/**
 * Storefront test for Cart at inSPIRED
 */
class CartSpec extends GebReportingSpec implements TestDataUsage
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
                    if(!productCartTable(name).removeLink().empty)
                    {
                        productCartTable(name).removeLink[0].click()
                    }
                }
        }
    }

    /**
     * Add Bundled Product to Cart<p>
     *
     * Old Smoke Test:
     * testAddBundledProductToCart(...)
     */
    def "Add Bundled Product to Cart"()
    {
        when: "I go to the product detail page of a product bundle"
        to ProductDetailPage, SKU:searchTerm
        
        then: "... find it at the Detail Page."
        at ProductDetailPage
        lookedForSKU searchTerm

        when: "I add it to Cart..."
        addToCart()

        then: "... and check all values I'm interested in."
        at CartPage
        checkProduct productName
        existingPLIs.push productName

        where:
        searchTerm  << testData.get("defaultProduct.bundle.subSku1")
        productName << testData.get("defaultProduct.bundle.subName1")
    }

    /**
     * Add Product Bundle to Cart<p>
     *
     * Old Smoke Test:
     * testAddProductBundleToCart(...)
     */
    def "Add Product Bundle to Cart"()
    {
        when: "I go to the product detail page of a product bundle"
        to ProductDetailPage, SKU:searchTerm
        
        then: "... find it at the Detail Page."
        at ProductDetailPage
        lookedForSKU searchTerm

        when: "I add it to Cart..."
        addToCart()

        then: "... and check it."
        at CartPage
        checkProduct productName
        existingPLIs.push productName

        where:
        searchTerm  << testData.get("defaultProduct.bundle.sku")
        productName << testData.get("defaultProduct.bundle.name")
    }

    /**
     * Add Retail Set to Cart<p>
     *
     * Old Smoke Test:
     * testAddRetailSetToCart(...)
     */
    def "Add Retail Set to Cart"()
    {
        when: "I go to the product detail page of a retail set"
        to ProductDetailPage, SKU:searchTerm
        then: "... find it at the Detail Page."
        at ProductDetailPage
        lookedForSKU searchTerm

        when: "I add it to Cart..."
        addToCart()

        then: "... and check it."
        at CartPage
        checkProduct retailObj1
        existingPLIs.push retailObj1
        checkProduct retailObj2
        existingPLIs.push retailObj2

        where:

        searchTerm  << testData.get("defaultProduct.retail.sku")
        retailObj1  << testData.get("defaultProduct.retail.subName1")
        retailObj2  << testData.get("defaultProduct.retail.subName2")

    }

    /**
     * Add only one product of Retail Set to Cart<p>
     */
    def "Add one Product of Retail Set to Cart"()
    {
        when: "I go to the product detail page of a retail set"
        to ProductDetailPage, SKU:searchTerm
        then: "... find it at the Detail Page."
        at ProductDetailPage

        when:
            lookedForSKU searchTerm
            setRetailSetQuantity retailObj2SKU,quantity0

        and: "I add it to Cart..."
            addToMiniCart()

        then: "... and check it."
        at CartPage
        checkProduct retailObj1
        existingPLIs.push retailObj1
        ! (checkProduct(retailObj2))

        where:

        searchTerm  << testData.get("defaultProduct.retail.sku")
        retailObj1  << testData.get("defaultProduct.retail.subName1")
        retailObj2  << testData.get("defaultProduct.retail.subName2")
        retailObj2SKU << testData.get("defaultProduct.retail.subSku2")
        quantity0    =  testData.get("defaultProduct.quantity.zero")[0]
    }

    /**
     * Edit Product in Cart<p>
     */
    def "Edit Cart"()
    {
        when: "I go to product detail page of first product"
        to ProductDetailPage, SKU:searchTerm1

        then: "check product detail page."
        at ProductDetailPage
        lookedForSKU searchTerm1
        BigDecimal priceProductOne = new BigDecimal(productPrice1)
        BigDecimal priceProductTwo = new BigDecimal(productPrice2)

        when: "I add it to Cart..."
        addToCart()

        then: "... and check it."
        at CartPage
        checkProduct productName1
        existingPLIs.push productName1

        when: "I go to product detail page of second product"
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

        when: "Now I change the quantity of both products and click update..."
        productCartTable(productName1).quantityInput.value 12
        productCartTable(productName2).quantityInput.value quantity
        updateButton.click()

        then: "... and check it."
        at CartPage
        // quantity input field of product two should contain current quantity value
         productCartTable(productName2).quantityInput.value() == (quantity+"")
        // total price of pli two should lift up regarding updated quantity
         $("div", text: '$ 190.00')
        // quantity input field of product one should contain current quantity value
         productCartTable(productName1).quantityInput.value() == ("12")
        // total price of pli one should lift up regarding updated quantity
        waitFor{ $("div", text: '$ 315.00').size()>0}

        when: "Now I change the quantity of the first product to zero and click update..."
        productCartTable(productName1).quantityInput.value zeroQuantity
        updateButton.click(CartPage)
        waitFor{ $("div", class: "alert-info").size()>0}

        then: "... and check it."
        at CartPage
        // pli one should not longer displayed in cart
         productCartTable(productName1).quantityInput.size() == 0
        // quantity input field of product two should contain current quantity value
         productCartTable(productName2).quantityInput.value() == (quantity+"")

        when: "Now I change the quantity of the second product to zero and click update..."
        productCartTable(productName2).quantityInput.value zeroQuantity
        updateButton.click(EmptyCartPage)

        then: "... and check it."
        at EmptyCartPage

        when: "I go to product detail page"
        to ProductDetailPage, SKU:searchTerm1

        then: "check product detail page."
        at ProductDetailPage
        lookedForSKU searchTerm1

        when: "I add it to Cart..."
        addToCart()

        then: "... and check it."
        at CartPage
        checkProduct productName1

        when: "Now I change the quantity with invalid value and click update..."
        productCartTable(productName1).quantityInput.value "a"
        updateButton.click()

        then: "... and check it."
        at CartPage
        // error message should displayed
        waitFor{ $("small", text:"Please enter a valid quantity.").size()>0}

        where:
        
        searchTerm1  = testData.get("cartProducts.product.sku")[0]
        searchTerm2  = testData.get("cartProducts.product.sku")[1]
        productName1 = testData.get("cartProducts.product.name")[0]
        productName2 = testData.get("cartProducts.product.name")[1]
        productPrice1 = testData.get("cartProducts.product.price")[0]
        productPrice2 = testData.get("cartProducts.product.price")[1]
        quantity    =  testData.get("cartProducts.product.quantity")[0]
        zeroQuantity = testData.get("cartProducts.product.quantity")[1]
        
    }

    /**
     * Delete Product from Cart<p>
     */
    def "Delete Product from Cart"()
    {
        when: "I go to product detail page"
        to ProductDetailPage, SKU:searchTerm

        then: "check product detail page."
        at ProductDetailPage
        lookedForSKU searchTerm
        BigDecimal productPrice = price

        when: "I add it to Cart..."
        addToCart()

        then: "... and check it."
        at CartPage
        checkProduct productName

        when: "Now I press the remove link..."
        productCartTable(productName).removeLink.click()

        then: "... and check it."
        at EmptyCartPage

        where:

        searchTerm  << testData.get("defaultProduct.default.sku")
        productName << testData.get("defaultProduct.default.name")
    }

    /**
     * Create Cart via Express Shop Cart<p>
     *
     * Old Smoke Test:
     * testCreateCartViaExpressShop(...)
     */
    def "Create Cart via Express Shop Cart"()
    {
        when: "I go to the home page, search for product..."
        to HomePage
        at HomePage
        header.search productName

        then: "... look for it at SearchPage."
        at SearchResultPage
        def productTile = productTiles(productName)

        when: "I click at ExpressIcon..."
        productTile.clickExpressShop();
        sleepForNSeconds(5);

        then: "... see ExpressView..."
        at ExpressShopModal

        when: "... and add to Cart."
        addToCartButton.click()
        existingPLIs.push productName

        then: "I back at SearchResult Page."
        at SearchResultPage

        when: "and can click at 'To Cart'"
        sleepForNSeconds(5);
        header.viewCartMiniCart()


        then: "Now it's there."
        at CartPage
        checkProduct productName

        where:
        productName  << testData.get("defaultProduct.promotion.name")
    }
}
