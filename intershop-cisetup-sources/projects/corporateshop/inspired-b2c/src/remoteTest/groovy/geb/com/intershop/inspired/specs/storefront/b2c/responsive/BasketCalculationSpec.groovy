package geb.com.intershop.inspired.specs.storefront.b2c.responsive

import geb.com.intershop.b2c.model.storefront.responsive.User
import geb.com.intershop.inspired.pages.backoffice.BackOfficeLoginPage
import geb.com.intershop.inspired.pages.backoffice.BackOfficePage
import geb.com.intershop.inspired.pages.backoffice.application.ApplicationGeneralTabPage
import geb.com.intershop.inspired.pages.backoffice.application.ApplicationShoppingCartPreferencePage
import geb.com.intershop.inspired.pages.backoffice.catalog.CatalogOverviewPage
import geb.com.intershop.inspired.pages.backoffice.channels.ApplicationsListPage
import geb.com.intershop.inspired.pages.backoffice.channels.BackOfficeChannelOverviewPage;
import geb.com.intershop.inspired.pages.backoffice.product.ProductDetailsPage
import geb.com.intershop.inspired.pages.backoffice.product.ProductDetailsPricingPage
import geb.com.intershop.inspired.pages.backoffice.product.ProductListChannelPage
import geb.com.intershop.inspired.pages.storefront.responsive.HomePage
import geb.com.intershop.inspired.pages.storefront.responsive.checkout.CartPage
import geb.com.intershop.inspired.pages.storefront.responsive.shopping.*
import geb.com.intershop.inspired.testdata.TestDataUsage
import geb.spock.GebReportingSpec
import spock.lang.*

class BasketCalculationSpec extends GebReportingSpec implements TestDataUsage
{    
    private static productID1
    private static productID2

    @Shared User b2cUser = new User("admin", "admin", "admin");
    @Shared String organization = "inSPIRED"
    
    def setupSpec()
    {
        setup:
        productID1 = testData.get("rest.calculation.product.sku")[0]
        productID2 = testData.get("rest.calculation.product.sku")[1]
    }
    
    def setup() {
        given: "Logged in into BackOffice as ${b2cUser.name} and inside the Consumer Channel" 
            to BackOfficeLoginPage
        when:       
            logInUser(b2cUser, organization)
        then:
            at BackOfficePage
            
        when: "I want to switch to the Consumer Channel"
            selectChannel("inTRONICS")
        then:
            at BackOfficeChannelOverviewPage
    }
    
    def cleanup() {
        given:
            to BackOfficeLoginPage
        when:
            logInUser(b2cUser, organization)
        then:
            at BackOfficePage
        when:
            selectChannel("inTRONICS")
      
            setProductTaxToFull()
        then:
            true
    }
    
    /*
     * =====================================================================
     * Test method to check "Consolidated Display Taxes"
     * =====================================================================
     */
    def "Test Consolidated Taxes Display"()
    {
        setProductTaxToReduced()
        homeButton.click()
        
        at BackOfficePage
        selectChannel("inTRONICS")
        
        setTaxDisplayToConsolidated()
        
        when: "Add two products to cart"
        setProductToCart(productID1)
        setProductToCart(productID2)

        then: "Check tax display in shopping cart page"
        at CartPage
        checkOrderSummaryLine "VAT incl. in Total"
    }

    /*
     * =====================================================================
     * Test method to check "Breakdown Display Taxes"
     * =====================================================================
     *
     */
    def "Test Breakdown Taxes Display"()
    {
        setProductTaxToReduced()

        homeButton.click()
        
        at BackOfficePage
        selectChannel("inTRONICS")

        setTaxDisplayToBreakdown()

        when: "Add two products to cart"
        setProductToCart(productID1)
        setProductToCart(productID2)
        
        then: "Check tax display in shopping cart page"
        at CartPage
        checkOrderSummaryLine "VAT incl. in Total 4.0 %"
        checkOrderSummaryLine "VAT incl. in Total 6.0 %"
    }
    
    /*
     * =====================================================================
     * Set product tax to reduced tax in commerce management
     * =====================================================================
     */
     private setProductTaxToReduced() {
         
         at BackOfficeChannelOverviewPage
         goToCatalogs()
 
         at CatalogOverviewPage
         goToProducts()
         
         at ProductListChannelPage
         searchProduct productID1
         openProduct productID1
 
         at ProductDetailsPage
         clickPricing()
         
         at ProductDetailsPricingPage
         applyReduced()
     }
     
     /*
      * =====================================================================
      * Set product tax to full tax in commerce management
      * =====================================================================
      */
      private setProductTaxToFull() {
          
          at BackOfficeChannelOverviewPage
          goToCatalogs()
  
          at CatalogOverviewPage
          goToProducts()
          
          at ProductListChannelPage
          searchProduct productID1
          openProduct productID1
  
          at ProductDetailsPage
          clickPricing()
          
          at ProductDetailsPricingPage
          applyDefault()
      }
     
     /*
      * =====================================================================
      * Set the "Display Taxes" setting to "Breakdown taxes"
      * =====================================================================
      */
     private setTaxDisplayToBreakdown() {

         at BackOfficeChannelOverviewPage
         goToApplications()

         at ApplicationsListPage
         clickOnApplicationName("B2C - Responsive")

         at ApplicationGeneralTabPage
         goToShoppingCartTab()

         at ApplicationShoppingCartPreferencePage
         applyBreakdown()
     }
     
     /*
      * =====================================================================
      * Set the "Display Taxes" setting to "Consolidated taxes"
      * =====================================================================
      */
     private setTaxDisplayToConsolidated() {
         
         at BackOfficeChannelOverviewPage
         goToApplications()
 
         at ApplicationsListPage
         clickOnApplicationName("B2C - Responsive")
 
         at ApplicationGeneralTabPage
         goToShoppingCartTab()
         
         at ApplicationShoppingCartPreferencePage
         applyDefault()
     }
     
     /*
      * =====================================================================
      * Add product to cart
      * =====================================================================
      */
     private setProductToCart(productID) {
         to ProductDetailPage, SKU:productID
         at ProductDetailPage
         lookedForSKU productID
         addToCart()
     }
}
