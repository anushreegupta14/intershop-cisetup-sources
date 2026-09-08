package geb.com.intershop.inspired.specs.storefront.b2c.responsive

import java.util.regex.Pattern

import geb.com.intershop.b2c.model.storefront.responsive.User
import geb.com.intershop.inspired.pages.backoffice.BackOfficeLoginPage
import geb.com.intershop.inspired.pages.backoffice.BackOfficePage
import geb.com.intershop.inspired.pages.backoffice.channels.BackOfficeChannelOverviewPage
import geb.com.intershop.inspired.pages.storefront.responsive.HomePage
import geb.com.intershop.inspired.pages.storefront.responsive.StorefrontPage
import geb.com.intershop.inspired.pages.storefront.responsive.checkout.CartPage
import geb.com.intershop.inspired.pages.storefront.responsive.shopping.CategoryPage
import geb.com.intershop.inspired.pages.storefront.responsive.shopping.FamilyPage
import geb.com.intershop.inspired.pages.storefront.responsive.shopping.ProductDetailPage
import geb.com.intershop.inspired.pages.storefront.responsive.sitemap.SitemapCategoryListPage
import geb.com.intershop.inspired.pages.storefront.responsive.sitemap.SitemapPage
import geb.com.intershop.inspired.pages.storefront.responsive.sitemap.SitemapProductListPage
import geb.com.intershop.inspired.testdata.TestDataUsage
import geb.module.Checkbox
import geb.spock.GebReportingSpec
import spock.lang.*

/**
 * Storefront tests URLRewrite handling
 *
    test url rewriting:
    - activate URL Rewriting in BO

    - call Homepage rewritten URL
        check compacted URLs on Homepage
    - call Product rewritten URL
    - call Category rewritten URL
    (call ViewData rule ?)
    call AboutUs rewritten URL
    call Sitemap rewritten URL on Homepage (for replacing HTMLUnit test seo.consumerstorefront.site.mapping.SiteMapTest, IS-18603) and navigate to at least one product
    test additional rules if necessary

    - deactivate URL Rewriting after test run

 */
// Quarantined because it influences other tests on bamboo and causes test failures with error message "URL invalid"
@Stepwise 
class URLRewriteHandlingSpec extends GebReportingSpec implements TestDataUsage
{
    
    @Shared
    private String rewrittenBaseUrl

    @Shared
    private User b2cUser
    
    def setupSpec()
    {
        setup:
        

        b2cUser = new User("admin", "admin", "admin")
        
        String rewriteSiteName = testData.get("seo.url.rewrite.site.name")[0]

        rewrittenBaseUrl = "/WFS".trim() + 
                           "/" + rewriteSiteName.trim() + 
                           "/" + System.getProperty("url.locale", "en_US").trim() +
                           "/" + System.getProperty("url.application", "-").trim() +
                           "/" + System.getProperty("url.currency" , "USD").trim();

    }

    /**
     * Start URL Rewriting tests - activate URL Rewriting in BO.
     * In fact, this is not a test but needed for the following tests.
     * 
     */
    @Requires({ Boolean.valueOf(env['run_quarantine']) })
    def "Activate URL Rewriting"() 
    {
        given: "Logged in as admin.."
            to BackOfficeLoginPage
        when:
            logInUser(b2cUser, "inSPIRED")
        then:
            at BackOfficePage
            
        when: "I want to switch to the B2C Channel"
            selectChannel("inTRONICS")
        then:
            at BackOfficeChannelOverviewPage
    
        when: "I go to the channel preferences"
            goToPreferences()            
        and: "Click SEO Settings"
            linkSEOSettings.click()
        then: "I am at the SEO Settings preference page and I can enable the URL Rewriting."
            checkboxURLRewriting.module(Checkbox).check()
            inputRewriteSiteName.value rewriteSiteName
            btnApply.click()
            
        where:
            rewriteSiteName = testData.get("seo.url.rewrite.site.name")[0]
            //logOutUser()
        
    }
    
    /**
     * URL Rewriting test for Homepage
     * This will use the pre-defined rewrite rule of type="Homepage"
     *
     * http://localhost:8081/INTERSHOP/web/WFS/inSPIRED-inTRONICS-Site/en_US/-/USD/ViewHomepage-Start
     * to
     * http://localhost:8081/WFS/hurz/en_US/-/USD/
     * 
     * 
     */
    @Requires({ Boolean.valueOf(env['run_quarantine']) })
    def "Test Homepage rule rewritten URL"() 
    {
        String shortUrl = rewrittenBaseUrl +
                          "/";
                      
        when:
            go makeURL(shortUrl)
        then:
            at HomePage
            
        //check compacted URLs on Homepage
    }
    
    /**
     * URL Rewriting test for some Product handling
     * 
     * First, check URL Rewriting for Product.
     * This will use the pre-defined rewrite rule of type="Product"
     *
     * http://localhost:8081/INTERSHOP/web/WFS/inSPIRED-inTRONICS-Site/en_US/-/USD/ViewProduct-Start?SKU=M8182790134362&CategoryName=832&CatalogID=Cameras-Camcorders&tracking=searchterm:M8182790134362
     * to
     * http://localhost:8081/WFS/hurz/en_US/-/USD/Cameras-Camcorders/832/gopro-hero4-silver-bundle-zidM8182790134362
     * 
     *  
     * Then, add this product to basket.
     * This will use the pre-defined rewrite rule of type="ViewData"
     *
     * https://localhost:451/INTERSHOP/web/WFS/inSPIRED-inTRONICS-Site/en_US/-/USD/ViewData-Start/1775316158?JumpTarget=ViewCart-View
     * to
     * https://localhost:451/WFS/hurz/en_US/-/USD/viewdata/1775316260?JumpTarget=ViewCart-View
     * 
     */
    @Requires({ Boolean.valueOf(env['run_quarantine']) })
    def "Test Product rule and ViewData rule rewritten URL"() 
    {
        def shortUrl = rewrittenBaseUrl + 
                       rewrittenProductUrl
                      
        def pos = rewrittenProductUrl.lastIndexOf("-zid");
        def sku = rewrittenProductUrl.substring(pos + 4);
        
        when:
            go makeURL(shortUrl)
        then:
            at ProductDetailPage
            
            assert lookedForTitle(productDisplayName)
            assert productSKU == sku
        
		when:
			addToCart()
		then:
			at CartPage
			
		assert page.driver.currentUrl.matches(".*" + Pattern.quote(rewrittenBaseUrl + "/viewdata/") + "\\w*" + Pattern.quote("?JumpTarget=ViewCart-View"))
		
        where:
            rewrittenProductUrl       = testData.get("seo.url.rewrite.product.rewritten")[0]
            productDisplayName        = testData.get("seo.url.rewrite.product.displayName")[0]
            
    }

    /**
     * URL Rewriting test for Category
     * This will use the pre-defined rewrite rule of type="Category"
     *
     * http://localhost:8081/INTERSHOP/web/WFS/inSPIRED-inTRONICS-Site/en_US/-/USD/ViewStandardCatalog-Browse?CatalogID=Cameras-Camcorders&CategoryName=577
     * to
     * http://localhost:8081/WFS/hurz/en_US/-/USD/Cameras-Camcorders/577/
     * 
     */
    @Requires({ Boolean.valueOf(env['run_quarantine']) })
    def "Test Category rule rewritten URL"() 
    {
        String shortUrl = rewrittenBaseUrl +
                       "/" +
                       catalog +
                       "/";

        when:
            go makeURL(shortUrl)
        then: "I am at the catalog page"
            at CategoryPage
            withCategory(catalog)

        and: "A subcategory 'Digital Cameras' exists"
            assert subCategoryLink(category).size() > 0

        when:
            go makeURL(shortUrl + category + "/")
        then: "I am at the category page"
            at FamilyPage
            assert $('[data-testing-id="family-page-' + category + '"]').size() > 0

        
        where:
            catalog        = testData.get("seo.url.rewrite.catalog")[0]
            category       = testData.get("seo.url.rewrite.category")[0]
            
    }

    /**
     * URL Rewriting test for AboutUs
     * This will use the pre-defined rewrite rule of type="PageGeneric"
     *
     * http://localhost:8081/INTERSHOP/web/WFS/inSPIRED-inTRONICS-Site/en_US/-/USD/ViewContent-Start?PageletEntryPointID=page.aboutus
     * to
     * http://localhost:8081/WFS/hurz/en_US/-/USD/about-us-cms-page.aboutus
     * 
     */
    @Requires({ Boolean.valueOf(env['run_quarantine']) })
    def "Test PageGeneric rule rewritten URL"() 
    {
        String shortUrl = rewrittenBaseUrl +
                       "/" +
                       shortPath

        when:
            go makeURL(shortUrl)
        then:
            at StorefrontPage

            assert title == pageTitle
            
        where:
            shortPath      = testData.get("seo.url.rewrite.aboutus.shortPath")[0]
            pageTitle      = testData.get("seo.url.rewrite.aboutus.pageTitle")[0]
            
    }

    /**
     * URL Rewriting test for "Terms & Conditions"
     * This will use the pre-defined rewrite rule of type="Page"
     *
     * https://localhost:451/INTERSHOP/web/WFS/inSPIRED-inTRONICS-Site/en_US/-/USD/ViewContent-Start?PageletEntryPointID=systempage.termsAndConditions.pagelet2-Page
     * to
     * http://localhost:8081/WFS/hurz/en_US/-/USD/terms-and-conditions
     * 
     */
    @Requires({ Boolean.valueOf(env['run_quarantine']) })
    def "Test Page rule rewritten URL"() 
    {
        String shortUrl = rewrittenBaseUrl +
                       "/" +
                       shortPath

        when:
            go makeURL(shortUrl)
        then:
            at StorefrontPage

            assert title == pageTitle

        where:
            shortPath      = testData.get("seo.url.rewrite.tnc.shortPath")[0]
            pageTitle      = testData.get("seo.url.rewrite.tnc.pageTitle")[0]
        
    }

    
    /**
     * URL Rewriting test for some Sitemap rewriting
     * 
     * First, go to the Sitemap base page. 
     * This will use the pre-defined rewrite rule of type="Pipeline"
     *
     * http://localhost:8081/INTERSHOP/web/WFS/inSPIRED-inTRONICS-Site/en_US/-/USD/ViewSitemap-Start
     * to
     * http://localhost:8081/WFS/hurz/en_US/-/USD/sitemap
     * 
     * 
     * Then go to a Sitemap category
     * This will use the pre-defined rewrite rule of type="RegEx"
     *
     * http://localhost:8081/WFS/hurz/en_US/-/USD/sitemap-category/Computers
     * to
     * http://localhost:8081/INTERSHOP/web/WFS/inSPIRED-inTRONICS-Site/en_US/-/USD/ViewSitemap-BrowseCategory?CatalogCategoryBOName=Computers
     * 
     * 
     * Now go for Sitemap products
     * This will use the pre-defined rewrite rule of type="SitemapProducts"
     *
     * http://localhost:8081/WFS/hurz/en_US/-/USD/sitemap-products/computers/notebooks-and-pcs/notebook-accessories/
     * to
     * https://localhost:451/INTERSHOP/web/WFS/inSPIRED-inTRONICS-Site/en_US/-/USD/ViewSitemap-ProductList?CatalogID=Computers&CategoryName=1284
     * 
     */
    @Requires({ Boolean.valueOf(env['run_quarantine']) })
    def "Test Pipeline rule, RegEx rule, SitemapProducts rule rewritten URLs"() 
    {
        // sitemap by Pipeline rewriting
        String shortUrl = rewrittenBaseUrl +
                       "/sitemap"

        when: "I click the 'Sitemap' link." 
            go makeURL(shortUrl)
        then: "I'm at the Sitemap page."
            at SitemapPage
            
        
        // sitemap category by RegEx rewriting    
        when: "Now I go to a Sitemap category."
            shortUrl = rewrittenBaseUrl +
                       "/sitemap-category/" +
                       sitemapCategory
        and:      
            go makeURL(shortUrl)
        then: 
            at SitemapCategoryListPage
   
            
        // sitemap products by SitemapProducts rewriting    
        when: "Now I go to a Sitemap category."
            shortUrl = rewrittenBaseUrl +
                       "/sitemap-products/" +
                       sitemapProductsPath
        and:      
            go makeURL(shortUrl)
        then: 
            at SitemapProductListPage
   
            
        where:
            sitemapCategory     = testData.get("seo.url.rewrite.sitemap.category")[0];
            sitemapProductsPath = testData.get("seo.url.rewrite.sitemap.products.path")[0];
            
        
        // (for replacing HTMLUnit test seo.consumerstorefront.site.mapping.SiteMapTest, IS-18603)
        // and navigate to at least one product
    }

     
   /**
     * Finish URL Rewriting tests - de-activate URL Rewriting in BO
     * 
     */
    @Requires({ Boolean.valueOf(env['run_quarantine']) })
    def "De-activate URL Rewriting"() 
    {
        given: "Logged in as admin.."
            to BackOfficeLoginPage
        when: 
            logInUser(b2cUser, "inSPIRED")
        then:
            at BackOfficePage
            
        when: "I want to switch to the B2C Channel"
            selectChannel("inTRONICS")
        then:
            at BackOfficeChannelOverviewPage
    
        when: "I go to the channel preferences"
            goToPreferences()            
        and: "Click SEO Settings"
            linkSEOSettings.click()
        and: "I am at the SEO Settings preference page and I can disable the URL Rewriting."
            checkboxURLRewriting.module(Checkbox).uncheck()
            inputRewriteSiteName.value "inTRONICS"
            btnApply.click()
            
            //logOutUser()
        then:
            true
    }

    /**
     * Create the complete URL string from a given page path.
     * Uses the current browser used by the Spec and it's defined base URL.
     *     
     * @param   pagePath  The page path
     * @return  String    The complete URL string
     */
    private String makeURL(String pagePath) {
        return getBrowser().getBaseUrl() + pagePath
    }
    
}
