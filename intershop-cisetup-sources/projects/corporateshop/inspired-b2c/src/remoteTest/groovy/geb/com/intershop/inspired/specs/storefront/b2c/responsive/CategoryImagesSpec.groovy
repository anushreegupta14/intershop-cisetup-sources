package geb.com.intershop.inspired.specs.storefront.b2c.responsive

import geb.com.intershop.inspired.pages.storefront.responsive.*
import geb.com.intershop.inspired.pages.storefront.responsive.account.*
import geb.com.intershop.inspired.pages.storefront.responsive.checkout.*
import geb.com.intershop.inspired.pages.storefront.responsive.shopping.*
import geb.com.intershop.inspired.testdata.TestDataUsage
import geb.spock.GebReportingSpec
import spock.lang.*


/**
 * Storefront tests for Category images defined by the demo data
 *
 */
class CategoryImagesSpec extends GebReportingSpec implements TestDataUsage
{
       
    /**
     * Check certain images
     *
     */
    def "Check certain category images"()
    {
        when: "I go to homepage"
        to HomePage
        then: "I am at the homepage"
        at HomePage
        
        def i = 0
        2.times{
            
            when: "Clicking a the " + categoryRoot[i] + " catalog link"
                clickCategoryLink(categoryRoot[i])
            then: "I am at the " + categoryRoot[i] + " category page"
                at CategoryPage
                withCategory(categoryRoot[i])
                   
            and: "A subcategory " + categoryId[i] + " with image"        
            assert subCategoryImage(categoryId[i], categoryImage[i]).size() > 0
      
            i += 1        
        }
        
        where:
        categoryRoot = testData.get("category.root.id")
        categoryId = testData.get("category.image.category.id")
        categoryImage = testData.get("category.image.category.image")
    }
    
}
