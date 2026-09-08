package geb.com.intershop.inspired.specs.storefront.b2c.responsive

import geb.com.intershop.inspired.pages.storefront.responsive.HomePage
import geb.com.intershop.inspired.pages.storefront.responsive.login.StorefrontLogin
import geb.com.intershop.inspired.pages.storefront.responsive.shopping.ProductDetailPage
import geb.com.intershop.inspired.testdata.TestDataUsage
import geb.spock.GebReportingSpec

/**
 * Tests the visibility of special prices at the storefront. 
 *
 */
class PricingSpec extends GebReportingSpec implements TestDataUsage, StorefrontLogin
{    
	def "Price is visible for unregistered customers only"()
	{
        when: "I go to product detail page"
        	to ProductDetailPage, SKU:sku
        
        then: "I am at the product detail page and see a special price for unregistered customers"
            at ProductDetailPage
            assert $("span[data-testing-id='scale-prices']")[0].text().contains('Buy 5 for $ 150.00 ($ 30.00 each)')
            
        // Test if the price is not visible for registered customers
        when: "I log in"
            logInStorefrontUser(email, password)
            
        and: "go to product detail page of a product that has a special price for unregistered customers"
           	to ProductDetailPage, SKU:sku
            
        then: "I am at the product detail page and I cannot see a special price as a registered customer"
            at ProductDetailPage
            assert $("span[data-testing-id='scale-prices']").isEmpty()
            
        where:
            email    = testData.get("pricing.patricia.email")[0]
            password = testData.get("pricing.password")[0]
            sku      = testData.get("pricing.specialPrice.unregisteredCustomers.sku")[0]
	}
    
    def "Price is visible for an individual customer only"()
    {
        when: "I log in as Joe Fender"
            logInStorefrontUser(email, password)
            
        and: "go to product detail page of a product that has a special price for Joe Fender"
           	to ProductDetailPage, SKU:sku
        
        then: "I am at the product detail page and see a special price"
            at ProductDetailPage
            assert $("span[data-testing-id='scale-prices']")[0].text().contains('Buy 2 for $ 74.00 ($ 37.00 each)')
            
        // Test if the price is not visible for other customers
        when: "I log out"
            logOutStorefrontUser()
            
        and: "go to product detail page of a product that has a special price for Joe Fender" 
           	to ProductDetailPage, SKU:sku
            
        then: "I am at the product detail page and cannot see a special price"
            at ProductDetailPage
            assert $("span[data-testing-id='scale-prices']").isEmpty()
              
        where:
            email    = testData.get("pricing.fender.email")[0]
            password = testData.get("pricing.password")[0]
            sku      = testData.get("pricing.specialPrice.fender.sku")[0]
    }
    
    def "Special price is visible for everyone"()
    {
       when: "I go to product detail page of a product with a special price for everyone"
           	to ProductDetailPage, SKU:sku
            
        then: "I am at the product detail page and can see a new and an old price"
            at ProductDetailPage
            assert $("div[data-testing-id='current-price']")[0].text().contains('$ 286.43')
            assert $("div[data-testing-id='old-price']")[0].text().contains('$ 318.25')
            
        where:
            sku = testData.get("pricing.specialPrice.everyone.sku")[0]
    }
}
