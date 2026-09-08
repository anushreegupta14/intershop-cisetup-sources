package geb.com.intershop.inspired.specs.storefront.b2c.responsive

import geb.com.intershop.inspired.pages.backoffice.BOLoginPage
import geb.com.intershop.inspired.pages.backoffice.channels.ChannelOverviewPage
import geb.com.intershop.inspired.pages.storefront.responsive.*
import geb.com.intershop.inspired.pages.storefront.responsive.account.*
import geb.com.intershop.inspired.pages.storefront.responsive.checkout.warranty.*
import geb.com.intershop.inspired.pages.storefront.responsive.shopping.warranty.*
import geb.com.intershop.inspired.testdata.TestDataUsage
import geb.module.Checkbox
import geb.spock.GebReportingSpec
import spock.lang.*


/**
 * Storefront test for HRefLang links.
 */
@Ignore
class HRefLangSpec extends GebReportingSpec implements TestDataUsage 
{
    private String productName;
    private String catalogName;
    private String userName;
    private String password;
    private String organization;
    private String applicationUrlIdentifier
    private String aboutUsLinkText;
    
    def setup()
    {
        productName = testData.get("hreflangspec.product.name").get(0);
        catalogName = testData.get("hreflangspec.catalog.name").get(0);
        userName = testData.get("hreflangspec.login.admin").get(0);
        password = testData.get("hreflangspec.login.admin").get(1);
        organization = testData.get("hreflangspec.login.admin").get(2);
        applicationUrlIdentifier = testData.get("hreflangspec.application.urlidentifier").get(0);
        aboutUsLinkText = testData.get("hreflangspec.link.text.aboutus").get(0);
    }
    
    //@Requires({ Boolean.valueOf(env['run_quarantine']) })
    def "Enable HRefLang links in the back office and check storefront"()
    {
       given: "Login page"
            to BOLoginPage
        
        when: "Login to back office"
            login userName, password, organization
            
        and: "Navigate to B2C channel"
            toB2CSalesChannel()
            
        then: "Click link Applications"
            at ChannelOverviewPage
        when:
            siteNavBar.linkApplications.click()
            
        and: "Click link of application with URLIdentifier '-'"
            clickApplication(applicationUrlIdentifier)
            
        and: "Add all available languages"
            addAllAvailableLanguages()
            
        and: "I go to the channel preferences"
            siteNavBar.linkPreferences.click()
        
        and: "Click SEO Settings"
            linkSEOSettings.click()
         
        then: "I am at the SEO Settings preference page and I can enable the HRefLang links"
            checkboxHRefLangLinks.module(Checkbox).check()
            btnApply.click()
            
        when: "I go to the storefront"
            to HomePage
            
        and: "Select a catalog"
            selectCatalog catalogName
            
        then: "I find HRefLang links within the head"
            assert $("head").children("link")*.@hreflang.size() > 0
        
        when: "I select a product"
            $("a", text: productName).click()
       
        then: "I find HRefLang links within the head"
            assert $("head").children("link")*.@hreflang.size() > 0
            
        when: "I click 'About Us' link"
            $("a", text: aboutUsLinkText).click()
            
        then: "I find HRefLang links within the head"
            assert $("head").children("link")*.@hreflang.size() > 0
    }
    
    def cleanup()
    {
        // Turn off HRefLang links
        to BOLoginPage
        login userName, password, organization
        toB2CSalesChannel()
        at ChannelOverviewPage
        siteNavBar.linkPreferences.click()
        linkSEOSettings.click()
        checkboxHRefLangLinks.module(Checkbox).uncheck()
        btnApply.click()
    }
}
