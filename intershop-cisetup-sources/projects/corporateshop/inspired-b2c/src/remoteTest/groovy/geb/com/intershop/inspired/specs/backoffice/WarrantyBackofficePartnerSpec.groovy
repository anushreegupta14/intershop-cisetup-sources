package geb.com.intershop.inspired.specs.backoffice

import geb.com.intershop.b2c.model.storefront.responsive.User
import geb.com.intershop.inspired.pages.backoffice.*
import geb.com.intershop.inspired.pages.backoffice.channels.reseller.BackOfficeResellerPage
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
class WarrantyBackofficePartnerSpec extends GebReportingSpec  implements TestDataUsage
{
	

    // Stacks for created test warranties and products, needed for cleanup at end of test.
    private static warrantyTestProducts = new Stack()
	@Shared User b2cUser = new User("admin", "admin", "admin");
    @Shared String organization = "inSPIRED"    

    /**
     * Runs after all tests in this Spec run once.
     * Deletes all new created test warranties and products to enable multiple runs.
     */
    def cleanupSpec()
    {
         given:
            to BackOfficeLoginPage
        
        when: "Logged in as admin.."
            logInUser(b2cUser, organization)
            
        and: "I want to switch to the Reseller Channel"
            selectChannel("Reseller Channel")
        then:
            at BackOfficeResellerPage

        // goto Products
        navigateToMainMenuItem "bo-sitenavbar-catalogs-reseller-channel", "link-catalogs-products-reseller-channel"
        
        at ProductListPage

        while ( !warrantyTestProducts.empty )
        {
            def sku = warrantyTestProducts.pop()
            deleteProduct sku
        }
        
        logoutUser()
    }

     /**
     * Runs before each test in this Spec will run. 
     * Go to catalog->products in backoffice.
     */
    def setup()
    {      
        given:
            to BackOfficeLoginPage
    
        when: "Logged in as admin.."
            logInUser(b2cUser, organization)
            
        and: "I want to switch to the Reseller Channel"
            selectChannel("Reseller Channel")
            
        then:
            at BackOfficeResellerPage

        // goto Products
        navigateToMainMenuItem "bo-sitenavbar-catalogs-reseller-channel", "link-catalogs-products-reseller-channel"
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
        
        and: "At the 'new product' detail page, I fill all my data and submit them."
        // at ProductNewPage
        setNameAndID warrantyName + namesuffix , warrantyID + namesuffix
        setOnline true
        setAvailable true
        btnApplyCreate.click()
        
        warrantyTestProducts.push (warrantyID + namesuffix)

        and: "At the 'new product' detail page, I click the 'Classifications' link."
        // at ProductDetailsGeneralPage
        clickClassifications()
        
        and: "At the 'Classifications' detail page, I click 'assign' at 'ServiceTypes'."
        // at ProductDetailsClassificationListPage
        clickAssignServiceType()
        
        and: "At the Service Type assignment page, I select 'Dependent Warranty' as type,...."
        // at ProductDetailsClassificationDetailsPage
        selectClassificationByName "Dependent Warranty"
        
        and: "... confirm selection, and apply default additional values, .."
        clickSelect()
        clickApplyAdditionalValues()
        
        and: "... click the 'Back to List' button ..."
        clickBackToList()
        
        and: "Back at the 'Classifications' detail page, I click at the 'Pricing' tab link..."
        // at ProductDetailsClassificationListPage
        clickPricing()
        
        and: "At the 'Pricing' tab for warranty products, I select the 'Fixed Price' price model..."
        // at ProductDetailsPricingWarrantyPage
        selectWarrantyPriceProviderByName "Fixed Price"
        
        and: ".. and click 'Apply' besides the price model selector."
        clickApplyPriceProvider()
  
        and: "At the Fixed Price details page, I provide a price value... "
        // at ProductDetailsPricingWarrantyFixedPage
        setWarrantyFixPriceValue(10)
        
        and: ".. click 'Add' to add the price entry."
        clickAddPriceEntry()
        
        and: "I click on 'Back to List'..."
        // clickBackToList()
        navigateToMainMenuItem "bo-sitenavbar-catalogs-reseller-channel", "link-catalogs-products-reseller-channel"
        
        
        then: ".. a warranty product with fixed price has been created. I'm back at the Products list."
        at ProductListPage 

        
        // now create a product        
        when: "I click at the New button..."
        clickNew()
        
        and: "At the 'new product' detail page, I fill all my data for a new product and submit them."
        // at ProductNewPage
        setNameAndID productName + namesuffix  ,productID + namesuffix
        setOnline true
        setAvailable true
        setWarrantyEligibleInput true
        btnApplyCreate.click()

        warrantyTestProducts.push (productID + namesuffix)

        and: "At the 'new product' detail page, I click the 'Pricing' link."
        // at ProductDetailsGeneralPage
        clickPricing()
        
        and: "At the 'Pricing' tab for products, I provide a price value and click 'Add'... "
        // at ProductDetailsPricingPage
        setListPriceValue(123.45)
        clickAddListPriceEntry()
        
        // assign the warranty to the product
        and: "I click on 'Links'."
        clickLinks()
        
        and: "At the 'Product links' tab, I click the 'Warranty' checkbox..."
        // at ProductDetailsLinksPage
        setCheckBoxWarranty()
        
        and: ".. and click the 'Assign' button."
        clickAssignForHasWarranty()
        
        and: "At the products selection search, I search, select and assign the warranty product created before."
        // at ProductsSelectionListPage 
        findAndAcceptProduct(warrantyID + namesuffix)
        
        then: " .. the warranty is assigned to this product."
        // at ProductDetailsLinksPage

        assert productLinksForHasWarranty.findAll().find{
            it.sku == (warrantyID + namesuffix)
        }
        
        
        // It's over        
        // "Done testing fixed price warranty... Back to Test."

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
        
        and: "At the 'new product' detail page, I fill all my data for a new product and submit them."
        // at ProductNewPage
        setNameAndID productName + namesuffix  ,productID + namesuffix
        setOnline true
        setAvailable true
        setWarrantyEligibleInput true
        btnApplyCreate.click()

        warrantyTestProducts.push (productID + namesuffix)

        and: "At the 'new product' detail page, I click the 'Pricing' link."
        // at ProductDetailsGeneralPage
        clickPricing()
        
        and: "At the 'Pricing' tab for products, I provide a price value and click 'Add'... "
        // at ProductDetailsPricingPage
        setListPriceValue(200.00)
        clickAddListPriceEntry()
        
        and: "I click on 'Back to List'..."
        navigateToMainMenuItem "bo-sitenavbar-catalogs-reseller-channel", "link-catalogs-products-reseller-channel"
        // clickBackToList()
        
        then: ".. a product has been created. I'm back at the Products list."
        at ProductListPage


        // Now create a warranty
        when: "I click at the New button..."
        clickNew()
        
        and: "At the 'new product' detail page, I fill all my data and submit them."
        // at ProductNewPage
        setNameAndID warrantyName + namesuffix , warrantyID + namesuffix
        setOnline true
        setAvailable true
        btnApplyCreate.click()
        
        warrantyTestProducts.push (warrantyID + namesuffix)

        and: "At the 'new product' detail page, I click the 'Classifications' link."
        // at ProductDetailsGeneralPage
        clickClassifications()
        
        and: "At the 'Classifications' detail page, I click 'assign' at 'ServiceTypes'."
        // at ProductDetailsClassificationListPage
        clickAssignServiceType()
        
        and: "At the Service Type assignment page, I select 'Dependent Warranty' as type,...."
        // at ProductDetailsClassificationDetailsPage
        selectClassificationByName "Dependent Warranty"
        
        and: "... confirm selection, and apply default additional values, .."
        clickSelect()
        clickApplyAdditionalValues()
        
        and: "... click the 'Back to List' button ..."
        clickBackToList()
        
        and: "Back at the 'Classifications' detail page, I click at the 'Pricing' tab link..."
        // at ProductDetailsClassificationListPage
        clickPricing()
        
        and: "At the 'Pricing' tab for warranty products, I select the 'Percentage' price model..."
        // at ProductDetailsPricingWarrantyPage
        selectWarrantyPriceProviderByName "Percentage"
        
        and: ".. and click 'Apply' besides the price model selector."
        clickApplyPriceProvider()
  
        and: "At the Percentage Price details page, I provide a percentage value and apply it... "
        // at ProductDetailsPricingWarrantyPercentagePage
        setWarrantyPercentageValue(10)
        clickApplyPercentageValue()

        
        // assign the warranty to the product
        and: "I click on 'Links'."
        clickLinks()
        
        and: "At the 'Product links' tab, I click the 'is Warranty of' checkbox..."
        // at ProductDetailsLinksPage
        setCheckBoxIsWarrantyOf()
        
        and: ".. I click the 'Assign' button..."
        clickAssignForWarrantyOf()
        
        and: "At the products selection search, I search, select and assign the product created before."
        // at ProductsSelectionListPage
        findAndAcceptProduct(productID + namesuffix)
        
        then: " .. the warranty is assigned to this product."
        // at ProductDetailsLinksPage

        assert productLinksForIsWarrantyOf.findAll().find{
            it.sku == (productID + namesuffix)
        }


        // It's over        
        // "Done testing percentage priced warranty... Back to Test."


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
        
        and: "At the 'new product' detail page, I fill all my data and submit them."
        // at ProductNewPage
        setNameAndID warrantyName + namesuffix , warrantyID + namesuffix
        setOnline true
        setAvailable true
        btnApplyCreate.click()
        
        warrantyTestProducts.push (warrantyID + namesuffix)

        and: "At the 'new product' detail page, I click the 'Classifications' link."
        // at ProductDetailsGeneralPage
        clickClassifications()
        
        and: "At the 'Classifications' detail page, I click 'assign' at 'ServiceTypes'."
        // at ProductDetailsClassificationListPage
        clickAssignServiceType()
        
        and: "At the Service Type assignment page, I select 'Dependent Warranty' as type,...."
        // at ProductDetailsClassificationDetailsPage
        selectClassificationByName "Dependent Warranty"
        
        and: "... confirm selection, and apply default additional values, .."
        clickSelect()
        clickApplyAdditionalValues()
        
        and: "... click the 'Back to List' button ..."
        clickBackToList()
        
        
        and: "Back at the 'Classifications' detail page, I click at the 'Pricing' tab link..."
        // at ProductDetailsClassificationListPage
        clickPricing()
        
        and: "At the 'Pricing' tab for warranty products, I select the 'Scaled Prices' price model..."
        // at ProductDetailsPricingWarrantyPage
        selectWarrantyPriceProviderByName "Scaled Prices"
        
        and: ".. and click 'Apply' besides the price model selector."
        clickApplyPriceProvider()
  
        and: "At the Scaled Prices details page, I add three price values... "
        // at ProductDetailsPricingWarrantyScaledPage
        setWarrantyScaledPriceValue(1000, 25)
        clickAddPriceEntry()
        setWarrantyScaledPriceValue(599.99, 19.99)
        clickAddPriceEntry()
        setWarrantyScaledPriceValue(99.99, 10.00)
        clickAddPriceEntry()
        
        and: ".. click 'Apply' for the warranty scales.."
        clickApplyScaledValues()
        
        
        and: "I click on 'Back to List'..."
        // clickBackToList()
        navigateToMainMenuItem "bo-sitenavbar-catalogs-reseller-channel", "link-catalogs-products-reseller-channel"
        
        then: ".. a warranty product with fixed price has been created. I'm back at the Products list."
        at ProductListPage 

        
        // now create a product        
        when: "I click at the New button..."
        clickNew()
        
        and: "At the 'new product' detail page, I fill all my data for a new product and submit them."
        // at ProductNewPage
        setNameAndID productName + namesuffix  ,productID + namesuffix
        setOnline true
        setAvailable true
        setWarrantyEligibleInput true
        btnApplyCreate.click()

        warrantyTestProducts.push (productID + namesuffix)

        and: "At the 'new product' detail page, I click the 'Pricing' link."
        // at ProductDetailsGeneralPage
        clickPricing()
        
        and: "At the 'Pricing' tab for products, I provide a price value and click 'Add'... "
        // at ProductDetailsPricingPage
        setListPriceValue(345.67)
        clickAddListPriceEntry()
        
        // assign the warranty to the product
        and: "I click on 'Links'."
        clickLinks()
        
        and: "At the 'Product links' tab, I click the 'Warranty' checkbox..."
        // at ProductDetailsLinksPage
        setCheckBoxWarranty()
        
        and: ".. and click the 'Assign' button."
        clickAssignForHasWarranty()
        
        and: "At the products selection search, I search, select and assign the warranty product created before."
        // at ProductsSelectionListPage 
        findAndAcceptProduct(warrantyID + namesuffix)
        
        then: " .. the warranty is assigned to this product."
        // at ProductDetailsLinksPage

        assert productLinksForHasWarranty.findAll().find{
            it.sku == (warrantyID + namesuffix)
        }


        // It's over
        // "Done testing scale priced warranty... Back to Test."


        where:
        warrantyName    << testData.get("warranty.price.scaled.warrantyProduct.name")
        warrantyID      << testData.get("warranty.price.scaled.warrantyProduct.id")
        productName     << testData.get("warranty.price.scaled.productWithWarranty.name")
        productID       << testData.get("warranty.price.scaled.productWithWarranty.id")
        
    }

    
}