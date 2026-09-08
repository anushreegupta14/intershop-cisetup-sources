package geb.com.intershop.inspired.specs.storefront.b2c.responsive

import geb.com.intershop.inspired.pages.backoffice.*
import geb.com.intershop.inspired.pages.storefront.responsive.*
import geb.com.intershop.inspired.pages.storefront.responsive.account.*
import geb.com.intershop.inspired.pages.storefront.responsive.checkout.*
import geb.com.intershop.inspired.pages.storefront.responsive.shopping.*
import geb.com.intershop.inspired.testdata.TestDataUsage
import geb.spock.GebReportingSpec
import spock.lang.*

/**
 * Storefront tests for Address Validation at inSPIRED
 *
 */
@Timeout(600)
class AddressSpec extends GebReportingSpec implements TestDataUsage
{    
    
    /**
     * On Customer Register Page check address validation with correct address<p>
     */
    def "Customer Register Page Validate Correct Address"()
    {
       
        when: "I go to the Homepage and press SignIn..."
        to HomePage
        at HomePage
        registerLink.click()

        then: "...to go to the RegisterPage."
        at AccountCreateFullPage
 
        when: "I fill address related fields."
        
        setAddress country,fName,lName,address1,address2,zip,city
        
        then: "Now Storefront Address Validation works."
        assert $("div.has-error").size() == 0;
        
        where:

        country << testData.get("address.validation.country")
        fName   << testData.get("address.validation.fName")
        lName   << testData.get("address.validation.lName")
        address1<< testData.get("address.validation.address1")
        address2<< testData.get("address.validation.address2")
        zip     << testData.get("address.validation.zip")        
        city    << testData.get("address.validation.city")

    }
    
    /**
     * On Customer Register Page check address validation with correct address<p>
     */
    def "Customer Register Page Validate Invalid Address Zip(3 digits)"()
    {
       
        when: "I go to the Homepage and press SignIn..."
        to HomePage
        at HomePage
        registerLink.click()

        then: "...to go to the RegisterPage."
        at AccountCreateFullPage
 
        when: "I fill address related fields."
        
        setAddress country,fName,lName,address1,address2,zip,city
        
        then: "Now Storefront Address Validation works."
        assert $("input", name :"AddressForm_PostalCode", 0).parents("div.has-error").size()==1;
        
        where:

        country << testData.get("address.validation.country")
        fName   << testData.get("address.validation.fName")
        lName   << testData.get("address.validation.lName")
        address1<< testData.get("address.validation.address1")
        address2<< testData.get("address.validation.address2")
        zip     << testData.get("address.validation.zip.invalid.3digit")        
        city    << testData.get("address.validation.city")

    } 
    
    /**
     * On Customer Register Page check address validation with correct address<p>
     */
    def "Customer Register Page Validate Invalid Address Zip(3digit2letter)"()
    {
       
        when: "I go to the Homepage and press SignIn..."
        to HomePage
        at HomePage
        registerLink.click()

        then: "...to go to the RegisterPage."
        at AccountCreateFullPage
 
        when: "I fill address related fields."
        
        setAddress country,fName,lName,address1,address2,zip,city
        
        then: "Now Storefront Address Validation works."
        assert $("input", name :"AddressForm_PostalCode", 0).parents("div.has-error").size()==1;
        
        where:

        country << testData.get("address.validation.country")
        fName   << testData.get("address.validation.fName")
        lName   << testData.get("address.validation.lName")
        address1<< testData.get("address.validation.address1")
        address2<< testData.get("address.validation.address2")
        zip     << testData.get("address.validation.zip.invalid.3digit2letter")
        city    << testData.get("address.validation.city")
    }
}
