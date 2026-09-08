package geb.com.intershop.inspired.specs.storefront.b2c.responsive

import geb.com.intershop.inspired.pages.backoffice.*
import geb.com.intershop.inspired.pages.backoffice.product.*
import geb.com.intershop.inspired.pages.storefront.responsive.*
import geb.com.intershop.inspired.pages.storefront.responsive.account.*
import geb.com.intershop.inspired.pages.storefront.responsive.checkout.*
import geb.com.intershop.inspired.pages.storefront.responsive.shopping.*
import geb.com.intershop.inspired.testdata.TestDataUsage
import geb.com.intershop.inspired.tools.email.*
import geb.spock.GebReportingSpec
import spock.lang.*
//import tests.remote.com.intershop.testfw.util.general.EmailUtils

/**
 * Storefront test for Min Cart Total Value at inSPIRED
 */
@Timeout(600)
@Ignore // ignore temporarily
class AttachmentsEmailSpec extends GebReportingSpec implements TestDataUsage
{
    def setupSpec()
    {        
        when: "I go to the backoffice..."
        to BOLoginPage
        at BOLoginPage

        then: "...navigate to B2C product list page..."
        navigateToB2CProducts()
                
        at ProductListPage
        then: "...and search for product..."
        searchProduct("8806086011815")
        
        then: "...and go to product detail page..."
        openProduct("8806086011815")
        
        at ProductDetailsPage
        then: "...go to attachments..."
        clickAttachments()
        
        at ProductDetailsAttachmentsPage
        then: "...lock product..."
        lockProduct()
        then: "...go to attachment details..."
        selectAttachmentByName("User manual")
        
        at ProductDetailsAttachmentDetailsPage
        then: "...set InEmail flag..."
        selectInEmailFlag()
        clickBack()
        
        at ProductDetailsAttachmentsPage
        then: "...go to attachment details..."
        selectAttachmentByName("EU Energy Label")
        
        at ProductDetailsAttachmentDetailsPage
        then: "...set InEmail flag to false..."
        deselectInEmailFlag()
        
    }

    /**
     * Add Bundled Product to Cart<p>
     *
     * Old Smoke Test:
     * testAddBundledProductToCart(...)
     */
    def "Add Bundled Product to Cart"()
    {
        when: "I go to the signIn page and log in..."
        to HomePage
        at HomePage
        pressLogIn()
        
        then:
        at AccountLoginPage

        when:
        login user,password

        then: "... then I'm logged in."
        at AccountPage

        when: "I go to product detail page"
        to ProductDetailPage, SKU:searchTerm

        then: "Detail Page."
        at ProductDetailPage
        lookedForSKU searchTerm

        when: "I add it to Cart..."
        addToCart()

        then: "... checkout ..."
        at CartPage
        checkOut()
        
        
        and: "...and be at Payment page."
        at CheckoutPaymentPage
        
        and:"...choose Pay on Delivery..."
        cashOnDelivery()
        
        then: "... review my Order..."
        at CheckoutReviewPage

        when: "...and submit."
        agreeCheckBox.click()
        submitButton.click()

        then:
        at CheckoutReceiptPage

        then:
        EmailUtils emailUtils = new EmailUtils()
        Email e = emailUtils.getLatestEmailbyUniqueID(user)
        
        String link = emailUtils.getUrlByPipelineFromEmail("ViewProductAttachment", e.content);
        
        URL url = new URL(link);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        int responseCode = httpConn.getResponseCode();
        
        String result = "fail";
        if(EmailUtils.parseStringFromEmailContent(link, "</", e.content).indexOf("User manual") > 0 && 
            httpConn.getContentType().equals("application/pdf;charset=utf-8") &&
            e.content.indexOf("ViewProductAttachment") == e.content.lastIndexOf("ViewProductAttachment"))
        {
            result = "success";
        }
        
        result.equals("success")
               
        where:
        searchTerm  << testData.get("defaultProductWithAttechments.sku")
        user        << testData.get("checkoutUser.login.eMail")
        password    << testData.get("checkoutUser.login.password")
        
    }
    
}
