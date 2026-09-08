package geb.com.intershop.inspired.specs.backoffice

import geb.com.intershop.inspired.pages.backoffice.*
import geb.com.intershop.inspired.pages.backoffice.product.*
import geb.com.intershop.inspired.pages.storefront.responsive.*
import geb.com.intershop.inspired.testdata.TestDataUsage
import geb.spock.GebReportingSpec
import spock.lang.*

/**
 * Backoffice tests for Warranties at inSPIRED.
 *
 *  Create a new product
 *  Assign to ServiceType catalog
 *  Configure the product (price, attributes, link to other product)
 *
 * @author Frank Koch
 *
 */
@Ignore("move to f_business or delete")
class WarrantyBackofficeSpec extends GebReportingSpec  implements TestDataUsage
{
	

    // Stacks for created test warranties and products, needed for cleanup at end of test.
    private static warrantyTestProducts = new Stack()
    

    /**
     * Runs after all tests in this Spec run once.
     * Deletes all new created test warranties and products to enable multiple runs.
     */
    def cleanupSpec()
    {
        when:
        toProducts()
        
        then: "...go to the products list..."
        at ProductListPage

        and: "...and delete every new one."
        while ( !warrantyTestProducts.empty )
        {
            def sku = warrantyTestProducts.pop()
            deleteProduct sku
        }
    }

     /**
     * Runs before each test in this Spec will run. 
     * Go to catalog->products in backoffice.
     */
    def setup()
    {
        when: "I log in at the backoffice and go to Products..."
        toProducts()

        then: "...I'm at the product list page."
        at ProductListPage

    }

    /**
     * Helper method to reuse the following steps: 
     * Log in as admin at the backoffice, change to the
     * b2c channel and jump to catalog->products
     */
    private toProducts()
    {
        to BOLoginPage

        at BOLoginPage
        login "admin","!InterShop00!","inSPIRED"

        toB2CSalesChannel()

        waitFor{$("a",href:contains("ViewOverview-ChannelCatalog")).size()>0 }
        $("a",href:contains("ViewOverview-ChannelCatalog"),0).click()

        waitFor{$("table.wrapper").find("a",text:contains("Products"),0).size()>0 }
        
      $("table.wrapper").find("a",text:contains("Products"),0).click()
    }

    /**
     * Create fixed price warranty<p>
     * 
	 * Create a new warranty product and configure it as fixed price warranty.
	 * Create a new product and assign the previously created warranty.
     */
   def "Create fixed price warranty"()
    {
        def namesuffix=System.currentTimeMillis().toString()
        
        at ProductListPage
        
        // First create a warranty
        when: "I click at the New button..."
		clickNew()
		
        then: "...I'm at the 'new product' detail page."
		at ProductNewPage
		
        when: "I fill all my datas and submit...."
        setNameAndID warrantyName + namesuffix , warrantyID + namesuffix
		setOnline true
		setAvailable true
        btnApplyCreate.click()
        warrantyTestProducts.push (warrantyID + namesuffix)

        then: "...I'm at the 'new product' detail page."
		at ProductDetailsGeneralPage
		
        when: "I click at the Classifications link ..."
		clickClassifications()
		
        then: "...I'm at the 'Classifications' detail page."
		at ProductDetailsClassificationListPage
		
		when: "I click 'assign' at 'ServiceTypes'..."
		clickAssignServiceType()
		
		then: "...I'm at the Service Type assignment page."
		at ProductDetailsClassificationDetailsPage
        
        when: "I select 'Dependent Warranty' as type...."
        selectClassificationByName "Dependent Warranty"
        
        and: "... confirm selection..."
        clickSelect()
        
        and: "... apply default additional values .."
        clickApplyAdditionalValues()
        
        and: "... click the 'Back to List' button ..."
        clickBackToList()
        
        then: "... I'm at the 'Classifications' detail page again."
        at ProductDetailsClassificationListPage
        
        when: "I click at the 'Pricing' tab link"
        clickPricing()
        
        then: "... I'm at the 'Pricing' tab for warranty products."
        at ProductDetailsPricingWarrantyPage
        
        when: "I select the 'Fixed Price' price model..."
        selectWarrantyPriceProviderByName "Fixed Price"
        
        and: ".. click 'Apply' besides the price model selector .."
        clickApplyPriceProvider()
  
        then: ".. I am at the Fixed Price details page."
        at ProductDetailsPricingWarrantyFixedPage
        
        when: "I provide a price value... "
        setWarrantyFixPriceValue(10)
        
        and: ".. click 'Add'.."
        clickAddPriceEntry()
        
        then: ".. I'm still at the 'Pricing' detail page for the warranty product."
        at ProductDetailsPricingWarrantyFixedPage
        
        when: "I click on 'Back to List'..."
        clickBackToList()
        
        then: ".. I'm back at the Products list."
        at ProductListPage 

        
        // now create a product        
        when: "I click at the New button..."
        clickNew()
        
        then: "...I'm at the 'new product' detail page."
        at ProductNewPage
        
        when: "I fill all my datas and submit...."
        setNameAndID productName + namesuffix  ,productID + namesuffix
        setOnline true
        setAvailable true
        setWarrantyEligibleInput true
        btnApplyCreate.click()
        warrantyTestProducts.push (productID + namesuffix)

        then: "...I'm at the 'new product' detail page."
        at ProductDetailsGeneralPage
        
        when: "I click at the 'Pricing' tab link"
        clickPricing()
        
        then: "... I'm at the 'Pricing' tab for products."
        at ProductDetailsPricingPage
        
        when: "I provide a price value... "
        setListPriceValue(123.45)
        
        and: ".. click 'Add'.."
        clickAddListPriceEntry()
        
        then: ".. I'm still at the 'Pricing' detail page for the product."
        at ProductDetailsPricingPage
        
        // assign the warranty to the product
        when: "I click on 'Links'..."
        clickLinks()
        
        then: ".. I get to the product links tab."
        at ProductDetailsLinksPage
        
        when: "I click the 'Warranty' checkbox..."
        setCheckBoxWarranty()
        
        and: ".. I click the 'Assign' button..."
        clickAssignForHasWarranty()
        
        then: ".. I'm at the products selection search."
        at ProductsSelectionListPage 

        when: "I search for the warranty product created before...."
        findAndAcceptProduct(warrantyID + namesuffix)
        
        then: " .. the warranty is assigned to this product."
        at ProductDetailsLinksPage

        assert productLinksForHasWarranty.findAll().find{
            it.sku == (warrantyID + namesuffix)
        }
        
        
        // It's over        
        when: "I click on 'Back to List'..."
        clickBackToList()
        
        then:
        at ProductListPage 

        and: "Done testing fixed price warranty... Back to Test."
        logout()

        where:
        warrantyName    << testData.get("warranty.price.fixed.warrantyProduct.name")
        warrantyID      << testData.get("warranty.price.fixed.warrantyProduct.id")
        productName     << testData.get("warranty.price.fixed.productWithWarranty.name")
        productID       << testData.get("warranty.price.fixed.productWithWarranty.id")
        
	}

    /**
     * Create percentage priced warranty<p>
     * 
	 * Create a new product.
	 * Create a new warranty product, configure it as percentage priced warranty
	 * and assign it to the previously created product.
     */
   def "Create percentage priced warranty"()
    {
        def namesuffix=System.currentTimeMillis().toString()
        
        at ProductListPage

        // first create a product
        when: "I click at the New button..."
        clickNew()
        
        then: "...I'm at the 'new product' detail page."
        at ProductNewPage
        
        when: "I fill all my datas and submit...."
        setNameAndID productName + namesuffix  ,productID + namesuffix
        setOnline true
        setAvailable true
        setWarrantyEligibleInput true
        btnApplyCreate.click()
        warrantyTestProducts.push (productID + namesuffix)
        
        then: "...I'm at the 'new product' detail page."
        at ProductDetailsGeneralPage
        
        when: "I click at the 'Pricing' tab link"
        clickPricing()
        
        then: "... I'm at the 'Pricing' tab for products."
        at ProductDetailsPricingPage
        
        when: "I provide a price value... "
        setListPriceValue(200.00)
        
        and: ".. click 'Add'.."
        clickAddListPriceEntry()
        
        then: ".. I'm still at the 'Pricing' detail page for the product."
        at ProductDetailsPricingPage
        
        when: "I click on 'Back to List'..."
        clickBackToList()
        
        then: ".. I'm back at the Products list."
        at ProductListPage
        

        // Now create a warranty
        when: "I click at the New button..."
        clickNew()
        
        then: "...I'm at the 'new product' detail page."
        at ProductNewPage
        
        when: "I fill all my datas and submit...."
        setNameAndID warrantyName + namesuffix , warrantyID + namesuffix
        setOnline true
        setAvailable true
        btnApplyCreate.click()
        warrantyTestProducts.push (warrantyID + namesuffix)
        
        then: "...I'm at the 'new product' detail page."
        at ProductDetailsGeneralPage
        
        when: "I click at the Classifications link ..."
        clickClassifications()
        
        then: "...I'm at the 'Classifications' detail page."
        at ProductDetailsClassificationListPage
        
        when: "I click 'assign' at 'ServiceTypes'..."
        clickAssignServiceType()
        
        then: "...I'm at the Service Type assignment page."
        at ProductDetailsClassificationDetailsPage
        
        when: "I select 'Dependent Warranty' as type...."
        selectClassificationByName "Dependent Warranty"
        
        and: "... confirm selection..."
        clickSelect()
        
        and: "... apply default additional values .."
        clickApplyAdditionalValues()
        
        and: "... click the 'Back to List' button ..."
        clickBackToList()
        
        then: "... I'm at the 'Classifications' detail page again."
        at ProductDetailsClassificationListPage
        
        when: "I click at the 'Pricing' tab link"
        clickPricing()
        
        then: "... I'm at the 'Pricing' tab for warranty products."
        at ProductDetailsPricingWarrantyPage
        
        when: "I select the 'Percentage' price model..."
        selectWarrantyPriceProviderByName "Percentage"
        
        and: ".. click 'Apply' besides the price model selector .."
        clickApplyPriceProvider()
        
        then: ".. I am at the Percentage Price details page."
        at ProductDetailsPricingWarrantyPercentagePage
        
        when: "I provide a percentage value... "
        setWarrantyPercentageValue(10)
        
        and: ".. click 'Apply for the percentage value'.."
        clickApplyPercentageValue()
        
        then: ".. I'm still at the 'Pricing' detail page for the warranty product."
        at ProductDetailsPricingWarrantyPercentagePage
        
        
        
        // assign the warranty to the product
        when: "I click on 'Links'..."
        clickLinks()
        
        then: ".. I get to the product links tab."
        at ProductDetailsLinksPage
        
        when: "I click the 'is Warranty of' checkbox..."
        setCheckBoxIsWarrantyOf()
        
        and: ".. I click the 'Assign' button..."
        clickAssignForWarrantyOf()
        
        then: ".. I'm at the products selection search."
        at ProductsSelectionListPage
        
        when: "I search for the product created before...."
        findAndAcceptProduct(productID + namesuffix)
        
        then: " .. the warranty is assigned to this product."
        at ProductDetailsLinksPage

        assert productLinksForIsWarrantyOf.findAll().find{
            it.sku == (productID + namesuffix)
        }
        

        // It's over        
        when: "I click on 'Back to List'..."
        clickBackToList()
        
        then:
        at ProductListPage 

        then: "Done testing percentage priced warranty... Back to Test."
        logout()


        where:
        warrantyName    << testData.get("warranty.price.percentage.warrantyProduct.name")
        warrantyID      << testData.get("warranty.price.percentage.warrantyProduct.id")
        productName     << testData.get("warranty.price.percentage.productWithWarranty.name")
        productID       << testData.get("warranty.price.percentage.productWithWarranty.id")
        
    }

    /**
     * Create scale priced warranty<p>
     * 
	 * Create a new warranty product and configure it as scale priced warranty.
	 * Create a new product and assign the previously created warranty.
     */
   def "Create scale priced warranty"()
    {
        def namesuffix=System.currentTimeMillis().toString()
        
        at ProductListPage
        
        // First create a warranty
        when: "I click at the New button..."
        clickNew()
        
        then: "...I'm at the 'new product' detail page."
        at ProductNewPage
        
        when: "I fill all my datas and submit...."
        setNameAndID warrantyName + namesuffix , warrantyID + namesuffix
        setOnline true
        setAvailable true
        btnApplyCreate.click()
        warrantyTestProducts.push (warrantyID + namesuffix)
        
        then: "...I'm at the 'new product' detail page."
        at ProductDetailsGeneralPage
        
        when: "I click at the Classifications link ..."
        clickClassifications()
        
        then: "...I'm at the 'Classifications' detail page."
        at ProductDetailsClassificationListPage
        
        when: "I click 'assign' at 'ServiceTypes'..."
        clickAssignServiceType()
        
        then: "...I'm at the Service Type assignment page."
        at ProductDetailsClassificationDetailsPage
        
        when: "I select 'Dependent Warranty' as type...."
        selectClassificationByName "Dependent Warranty"
        
        and: "... confirm selection..."
        clickSelect()
        
        and: "... apply default additional values .."
        clickApplyAdditionalValues()
        
        and: "... click the 'Back to List' button ..."
        clickBackToList()
        
        then: "... I'm at the 'Classifications' detail page again."
        at ProductDetailsClassificationListPage
        
        when: "I click at the 'Pricing' tab link"
        clickPricing()
        
        then: "... I'm at the 'Pricing' tab for warranty products."
        at ProductDetailsPricingWarrantyPage
        
        when: "I select the 'Scaled Prices' price model..."
        selectWarrantyPriceProviderByName "Scaled Prices"
        
        and: ".. click 'Apply' besides the price model selector .."
        clickApplyPriceProvider()
        
        then: ".. I am at the Scaled Prices details page."
        at ProductDetailsPricingWarrantyScaledPage
        
        and: "I provide a price value... "
        setWarrantyScaledPriceValue(1000, 25)
        
        and: ".. click 'Add'.."
        clickAddPriceEntry()
        
        and: "I provide a price value... "
        setWarrantyScaledPriceValue(599.99, 19.99)
        
        and: ".. click 'Add'.."
        clickAddPriceEntry()
        
        when: "I provide a price value... "
        setWarrantyScaledPriceValue(99.99, 10.00)
        
        and: ".. click 'Add'.."
        clickAddPriceEntry()
        
        and: ".. click 'Apply' for the warranty scales.."
        clickApplyScaledValues()
        
        then: ".. I'm still at the 'Pricing' detail page for the warranty product."
        at ProductDetailsPricingWarrantyScaledPage
        
        
        when: "I click on 'Back to List'..."
        clickBackToList()
        
        then: ".. I'm back at the Products list."
        at ProductListPage



        // now create a product
        when: "I click at the New button..."
        clickNew()
        
        then: "...I'm at the 'new product' detail page."
        at ProductNewPage
        
        when: "I fill all my datas and submit...."
        setNameAndID productName + namesuffix  ,productID + namesuffix
        setOnline true
        setAvailable true
        setWarrantyEligibleInput true
        btnApplyCreate.click()
        warrantyTestProducts.push (productID + namesuffix)
        
        then: "...I'm at the 'new product' detail page."
        at ProductDetailsGeneralPage
        
        when: "I click at the 'Pricing' tab link"
        clickPricing()
        
        then: "... I'm at the 'Pricing' tab for products."
        at ProductDetailsPricingPage
        
        when: "I provide a price value... "
        setListPriceValue(345.67)
        
        and: ".. click 'Add'.."
        clickAddListPriceEntry()
        
        then: ".. I'm still at the 'Pricing' detail page for the product."
        at ProductDetailsPricingPage
        
        // assign the warranty to the product
        when: "I click on 'Links'..."
        clickLinks()
        
        then: ".. I get to the product links tab."
        at ProductDetailsLinksPage
        
        when: "I click the 'Warranty' checkbox..."
        setCheckBoxWarranty()
        
        and: ".. I click the 'Assign' button..."
        clickAssignForHasWarranty()
        
        then: ".. I'm at the products selection search."
        at ProductsSelectionListPage
        
        when: "I search for the warranty product created before...."
        findAndAcceptProduct(warrantyID + namesuffix)
        
        then: " .. the warranty is assigned to this product."
        at ProductDetailsLinksPage

        assert productLinksForHasWarranty.findAll().find{
            it.sku == (warrantyID + namesuffix)
        }
        
        
        // It's over
        when: "I click on 'Back to List'..."
        clickBackToList()
        
        then:
        at ProductListPage
        
        then: "Done testing scale priced warranty... Back to Test."
        logout()
        
        where:
        warrantyName    << testData.get("warranty.price.scaled.warrantyProduct.name")
        warrantyID      << testData.get("warranty.price.scaled.warrantyProduct.id")
        productName     << testData.get("warranty.price.scaled.productWithWarranty.name")
        productID       << testData.get("warranty.price.scaled.productWithWarranty.id")
        
    }

}
