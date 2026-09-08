package geb.com.intershop.inspired.specs.storefront.b2c.responsive

import geb.com.intershop.inspired.pages.storefront.responsive.HomePage
import geb.com.intershop.inspired.pages.storefront.responsive.shopping.SearchResultPage
import geb.com.intershop.inspired.testdata.TestDataUsage
import geb.spock.GebReportingSpec

class ContentSearchSpec  extends GebReportingSpec implements TestDataUsage 
{
    def "Search for Content Index Results"() 
    {
        when: "I go to the homepage"
        to HomePage
        and: "Search for '{$searchTerm}' returning only content results "
        header.search(searchTerm)

        then: "... I'm at the Search Result Page...."
        at SearchResultPage
        
        and: "the content result radio button is active"
        contentResultRadio*.@checked
        and: "the product result radio button is disabled"
        productResultRadio*.@disabled

        where:
        searchTerm  = testData.get("content.searchTerm")[0]
    }
    
    def "Search for Single Product - Multiple Content"()
    {
        when: "I go to the homepage"
        to HomePage
        and: "Search for '{$searchTerm}' returning only content results "
        header.search(searchTerm)

        then: "... I'm at the Search Result Page...."
        at SearchResultPage
        
        and: "the product result radio button is active"
        productResultRadio*.@checked
        
        and: "the product result is displayed"
        $("div.search-product-list")*.displayed
        
        when: "the content result radio is clicked"
        contentResultRadio*.click()
        
        then: "the content result is displayed"
        $("ul.search-content-list").displayed
        
        where:
        searchTerm  = testData.get("content.productName")[0]

    }

}
