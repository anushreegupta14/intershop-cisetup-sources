package geb.com.intershop.inspired.specs.storefront.b2c.responsive

import geb.com.intershop.inspired.pages.storefront.responsive.*
import geb.com.intershop.inspired.pages.storefront.responsive.account.*
import geb.com.intershop.inspired.pages.storefront.responsive.checkout.*
import geb.com.intershop.inspired.pages.storefront.responsive.shopping.*
import geb.com.intershop.inspired.testdata.TestDataUsage
import geb.spock.GebReportingSpec
import spock.lang.*


/**
 * Storefront tests for Category rich content
 *
 */
class CategoryRichContentSpec extends GebReportingSpec implements TestDataUsage
{
    /**
     * Check rich content on family page Cameras - digital Cameras 
     *
     */
    def "Check rich content on digital cameras family page"()
    {
        when: "I go to the signIn page and log in..."
        to HomePage
        then: "I am at the homepage"
            at HomePage
        
        when: "Clicking a the 'Cameras' catalog link"
        clickCategoryLink(categoryId1)

        then: "I am at the 'Cameras' category page"
        at CategoryPage
        withCategory(categoryId1)

        and: "A subcategory 'Digital Cameras' exists"
        assert subCategoryLink(categoryId2).size() > 0

        when: "Goto 'Digital Cameras' page"
        subCategoryLink(categoryId2).click()

        then: "I am at the Digital Cameras family page"
        at FamilyPage
        
        and: "The expected rich content is found"
        assert richContent.text().contains(categoryRichContent)
        
        where:
        categoryId1 = testData.get("categoryRichContent.camerasCamcorders.id")[0]
        categoryId2 = testData.get("categoryRichContent.digitalCameras.id")[0]
        categoryRichContent = testData.get("categoryRichContent.digitalCameras.text")[0]
        
    }
    
    /**
     * Check rich content on category page computers-tablets
     *
     */
    def "Check rich content on tablets category page"()
    {
        when: "I go to the signIn page and log in..."
        to HomePage
        then: "I am at the homepage"
            at HomePage
        
        when: "Clicking a the 'Computers' catalog link"
        clickCategoryLink(categoryId1)

        then: "I am at the 'Computers' category page"
        at CategoryPage
        withCategory(categoryId1)

        and: "A subcategory 'Tablets' exists"
        assert subCategoryLink(categoryId2).size() > 0

        when: "Goto 'Tablets' page"
        subCategoryLink(categoryId2).click()

        then: "I am at the Tablets category page"
        at CategoryPage
        
        and: "The expected rich content is found"
        assert richContent.text().contains(categoryRichContent)
        
        where:
        categoryId1 = testData.get("categoryRichContent.computers.id")[0]
        categoryId2 = testData.get("categoryRichContent.tablets.id")[0]
        categoryRichContent = testData.get("categoryRichContent.tablets.text")[0]
        
    }

}
