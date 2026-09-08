package geb.com.intershop.inspired.specs.storefront.b2c.rest

import geb.com.intershop.inspired.pages.backoffice.*
import geb.com.intershop.inspired.testdata.TestDataUsage
import geb.spock.GebReportingSpec
import groovy.json.JsonBuilder
import groovyx.net.http.RESTClient
import spock.lang.*


@Narrative("""
As a developer
I want to use the webform validation for 
address handeling via Intershop REST api
""")
@Title("Test webform validation for address handeling through the REST api")
@Ignore
class AddressValidationRestSpec extends GebReportingSpec implements TestDataUsage 
{

    @Shared
    RESTClient client = new RESTClient()
    @Shared
    RESTClient smbClient = new RESTClient()
    
    private static Map<String,List> testData
    private static String baseURI
    private static String smbBaseURI
    
    private static String pseudoNum = new Random().nextInt(10 ** 4) + ""
    private static String basketID
    private static String customerID
    private static String customerAddressURI
    private static String smbBasketID
    private static String smbCustomerID
    private static String smbCustomerAddressURI
    
    private static String user
    private static String smbUser
    private static String password   
    private static String hostName
    private static String port
    
    def setupSpec() {
        
        setup:

        user = testData.get("rest.validation.eMail")[0]
        smbUser = testData.get("rest.validation.smbEmail")[0]
        password = testData.get("rest.validation.password")[0]
                
        baseURI = baseUrl + testData.get("rest.validation.base.url")[0]
        smbBaseURI = baseUrl + testData.get("rest.validation.smb.base.url")[0]
        
        client.headers['Content-Type'] = testData.get("rest.validation.contentType")[0]
        client.headers['AuthToken'] = testData.get("rest.validation.authToken")[0]
        client.headers['Authorization'] = 'Basic ' + (user + ':' + password).bytes.encodeBase64().toString()
        
        smbClient.headers['Content-Type'] = testData.get("rest.validation.contentType")[0]
        smbClient.headers['AuthToken'] = testData.get("rest.validation.smbAuthToken")[0]
        smbClient.headers['Authorization'] = 'Basic ' + (smbUser + ':' + password).bytes.encodeBase64().toString()
        
        when: "I go to the backoffice..."
            to BOLoginPage
        then:
            at BOLoginPage

        when: "...and unable CAPTCHAs for Registration."
            uncheckB2CCAPTCHAs()
            uncheckSMBCAPTCHAs()
        then:
            true    
       
    }

    def cleanupSpec() {
        
        when: "I go to the backoffice..."
            to BOLoginPage
        then:
            at BOLoginPage

        when: "...and unable CAPTCHAs for Registration."
            checkB2CCAPTCHAs()
            checkSMBCAPTCHAs()
        then:
            true
      
    }
    
    static class JSONRequest
    {  
        
        String customerNo = "newCustomer_" + pseudoNum;
        String smbCustomerNo = "newSMBCustomer_" + pseudoNum;
        
        String requestBodyCreateCorrectBasketAddress = new JsonBuilder(
        {
           invoiceToAddress {
               id  testData.get("rest.validation.id")[0]
               state testData.get("rest.validation.state")[0]
               country testData.get("rest.validation.country")[0]
               postalCode testData.get("rest.validation.zip")[0]
               city testData.get("rest.validation.city")[0]
               street testData.get("rest.validation.address1")[0]
               street2 testData.get("rest.validation.address2")[0]
               firstName testData.get("rest.validation.fName")[0]
               lastName  testData.get("rest.validation.lName")[0]
               title testData.get("rest.validation.title")[0]
               countryCode testData.get("rest.validation.countryCode")[0]
               email  testData.get("rest.validation.eMail")[0]
               webformResource testData.get("rest.validation.webformResource")[0]
               webformName  testData.get("rest.validation.webformName")[0]
           }
        }).toString()
        
        String requestBodyCreateCorrectBasketAddressWithExtension = new JsonBuilder(
        {
           invoiceToAddress {
               id  testData.get("rest.validation.id")[0]
               state testData.get("rest.validation.state")[0]
               country testData.get("rest.validation.country")[0]
               postalCode testData.get("rest.validation.zip")[0]
               city testData.get("rest.validation.city")[0]
               street testData.get("rest.validation.address1")[0]
               street2 testData.get("rest.validation.address2")[0]
               firstName testData.get("rest.validation.fName")[0]
               lastName  testData.get("rest.validation.lName")[0]
               company testData.get("rest.validation.companyName")[0]
               title testData.get("rest.validation.title")[0]
               countryCode testData.get("rest.validation.countryCode")[0]
               email  testData.get("rest.validation.eMail")[0]
               webformResource testData.get("rest.validation.webformResource")[0]
               webformName  testData.get("rest.validation.webformName")[0]
               webformExtensionsResources  testData.get("rest.validation.webformExtensionsResources")
               webformExtensionsNames  testData.get("rest.validation.webformExtensionsNames")
           }
        }).toString()
        
        String requestBodyCreateInvalidBasketAddress = new JsonBuilder(
        {
           invoiceToAddress {
               id  testData.get("rest.validation.id")[0]
               state testData.get("rest.validation.state")[0]
               country testData.get("rest.validation.country")[0]
               postalCode testData.get("rest.validation.zip.invalid.3digit")[0]
               city testData.get("rest.validation.city")[0]
               street testData.get("rest.validation.address1")[0]
               street2 testData.get("rest.validation.address2")[0]
               firstName testData.get("rest.validation.fName")[0]
               lastName  testData.get("rest.validation.lName")[0]
               title testData.get("rest.validation.title")[0]
               countryCode testData.get("rest.validation.countryCode")[0]
               email  testData.get("rest.validation.eMail")[0]
               webformResource testData.get("rest.validation.webformResource")[0]
               webformName  testData.get("rest.validation.webformName")[0]
           }
        }).toString()
        
        String requestBodyCreateInvalidBasketAddressWithExtension = new JsonBuilder(
        {
           invoiceToAddress {
               id  testData.get("rest.validation.id")[0]
               state testData.get("rest.validation.state")[0]
               country testData.get("rest.validation.country")[0]
               postalCode testData.get("rest.validation.zip")[0]
               city testData.get("rest.validation.city")[0]
               street testData.get("rest.validation.address1")[0]
               street2 testData.get("rest.validation.address2")[0]
               firstName testData.get("rest.validation.fName")[0]
               lastName  testData.get("rest.validation.lName")[0]
               company testData.get("rest.validation.invalidCompanyName")[0]
               title testData.get("rest.validation.title")[0]
               countryCode testData.get("rest.validation.countryCode")[0]
               email  testData.get("rest.validation.eMail")[0]
               webformResource testData.get("rest.validation.webformResource")[0]
               webformName  testData.get("rest.validation.webformName")[0]
               webformExtensionsResources  testData.get("rest.validation.webformExtensionsResources")
               webformExtensionsNames  testData.get("rest.validation.webformExtensionsNames")
           }
        }).toString()
                
        String requestBodyCreateCustomerAddress = new JsonBuilder(
        {           
           customerNo  this.customerNo
           birthday testData.get("rest.validation.birthday")[0]
           preferredLanguage testData.get("rest.validation.preferredLanguage")[0] 
           firstName testData.get("rest.validation.fName")[0]
           lastName  testData.get("rest.validation.lName")[0]
           title testData.get("rest.validation.title")[0]
           email  this.customerNo + "@test.net"               
           credentials {
               login   this.customerNo + "@test.net"
               password    testData.get("rest.validation.password")[0]
               securityQuestion  testData.get("rest.validation.securityQuestion")[0]
               securityQuestionAnswer testData.get("rest.validation.securityQuestionAnswer")[0]
           }               
           address {
               state testData.get("rest.validation.state")[0]
               country testData.get("rest.validation.country")[0]
               postalCode testData.get("rest.validation.zip")[0]
               city testData.get("rest.validation.city")[0]
               street testData.get("rest.validation.address1")[0]
               street2 testData.get("rest.validation.address2")[0]
               firstName testData.get("rest.validation.fName")[0]
               lastName  testData.get("rest.validation.lName")[0]
               title testData.get("rest.validation.title")[0]
               countryCode testData.get("rest.validation.countryCode")[0]
               email  testData.get("rest.validation.eMail")[0]
               webformResource testData.get("rest.validation.webformResource")[0]               
               webformName  testData.get("rest.validation.webformName")[0]
           }
        }).toString() 
        
        String requestBodyCreateSMBCustomerAddress = new JsonBuilder(
        {
           customerNo  this.smbCustomerNo
           type testData.get("rest.validation.type")[0]
           companyName  testData.get("rest.validation.companyName")[0]
           industry testData.get("rest.validation.industry")[0]
           taxationID  testData.get("rest.validation.taxationID")[0]
           description testData.get("rest.validation.description")[0]
           credentials {
               type testData.get("rest.validation.credentialType")[0]
               login   this.smbCustomerNo + "@test.net"
               password    testData.get("rest.validation.password")[0]
               securityQuestion  testData.get("rest.validation.securityQuestion")[0]
               securityQuestionAnswer testData.get("rest.validation.securityQuestionAnswer")[0]
           }
           address {
               type testData.get("rest.validation.addressType")[0]               
               company  testData.get("rest.validation.companyName")[0]
               state testData.get("rest.validation.state")[0]
               country testData.get("rest.validation.country")[0]
               postalCode testData.get("rest.validation.zip")[0]
               city testData.get("rest.validation.city")[0]
               street testData.get("rest.validation.address1")[0]
               street2 testData.get("rest.validation.address2")[0]
               firstName testData.get("rest.validation.fName")[0]
               lastName  testData.get("rest.validation.lName")[0]
               countryCode testData.get("rest.validation.countryCode")[0]
               webformResource testData.get("rest.validation.webformResource")[0]                   
               webformName  testData.get("rest.validation.webformName")[0]
               webformExtensionsResources  testData.get("rest.validation.webformExtensionsResources")
               webformExtensionsNames  testData.get("rest.validation.webformExtensionsNames")
           }
           user {
               type testData.get("rest.validation.userType")[0]               
               preferredLanguage testData.get("rest.validation.preferredLanguage")[0]
               firstName testData.get("rest.validation.fName")[0]
               lastName  testData.get("rest.validation.lName")[0]
               title testData.get("rest.validation.title")[0]
               email  this.smbCustomerNo + "@test.net"
               businessPartnerNo this.smbCustomerNo
           }
        }).toString()
            
        String requestBodyUpdateCustomerAddress = new JsonBuilder(
        {
               state testData.get("rest.validation.state")[0]
               postalCode testData.get("rest.validation.zip")[0]
               country testData.get("rest.validation.country")[0]
               city testData.get("rest.validation.city")[0]
               street testData.get("rest.validation.address1")[0]
               street2 testData.get("rest.validation.address2")[0]
               firstName testData.get("rest.validation.fName")[0]
               lastName  testData.get("rest.validation.lName")[0]
               title testData.get("rest.validation.title")[0]
               countryCode testData.get("rest.validation.countryCode")[0]
               email  testData.get("rest.validation.eMail")[0]
               webformResource testData.get("rest.validation.webformResource")[0]
               webformName  testData.get("rest.validation.webformName")[0]
          
        }).toString()
    }
            
    def updateBasketID(newValue) {
        basketID = newValue
    }
    
    def updateCustomerAddressURI(newValue) {
        customerAddressURI = newValue
    }
    
    def updateCustomerID(newValue) {
        customerID = newValue
    }
    
    def updateSMBBasketID(newValue) {
        smbBasketID = newValue
    }
    
    def updateSMBCustomerAddressURI(newValue) {
        smbCustomerAddressURI = newValue
    }
    
    def updateSMBCustomerID(newValue) {
        smbCustomerID = newValue
    }
   
    
    def "Create New Basket"() {
        
        
        println ":::" + baseURI
        
        when: "send request to server"
            def createResponse = client.post(uri: baseURI + "baskets", requestContentType: testData.get("rest.validation.contentType")[0])
        
        then: "check response"        
            assert createResponse.success
            assert createResponse.responseData.title != null
            updateBasketID createResponse.responseData.title             
    }
    
    def "Create New SMB Basket"() {
        
        when: "send request to server"        
            def createResponse = smbClient.post(uri: smbBaseURI + "baskets", requestContentType: testData.get("rest.validation.contentType")[0])
        
        then: "check response"
            assert createResponse.success
            assert createResponse.responseData.title != null
            updateSMBBasketID createResponse.responseData.title
    }
    
    @Ignore
    def "Add New Address To Basket"() {
        
        when: "send request to server"        
            def createResponse = client.put(uri: baseURI + "baskets/" + basketID, requestContentType: testData.get("rest.validation.contentType")[0], body: request.requestBodyCreateCorrectBasketAddress)
     
        then: "check response"        
            assert createResponse.success
            assert createResponse.status == 200
                                
        where:        
            request << [new JSONRequest()]
    }
    
    @Ignore
    def "Add New Address To SMB Basket With Extension"() {
        
        when: "send request to server"
            def createResponse = smbClient.put(uri: smbBaseURI + "baskets/" + smbBasketID, requestContentType: testData.get("rest.validation.contentType")[0], body: request.requestBodyCreateCorrectBasketAddressWithExtension)
     
        then: "check response"
            assert createResponse.success
            assert createResponse.status == 200
                                
        where:
            request << [new JSONRequest()]
    }
    
    def "Add New Address To Basket With Invalid PostalCode(3-digits)"() {       
        
        def responseCode
        
        client.handler.failure = { responseReader ->
            responseCode = responseReader.status
         }
        
        when: "send request to server"
            def createResponse = client.put(uri: baseURI + "baskets/" + basketID, requestContentType: testData.get("rest.validation.contentType")[0], body: request.requestBodyCreateInvalidBasketAddress)
     
        then: "check response"            
            assert responseCode == 400
                                
        where:
            request << [new JSONRequest()]
    }
    
    def "Add New Address To SMB Basket With Extension And Invalid Company Name"() {
        
        def responseCode
        
        smbClient.handler.failure = { responseReader ->
            responseCode = responseReader.status
         }
        
        when: "send request to server"
            def createResponse = smbClient.put(uri: smbBaseURI + "baskets/" + smbBasketID, requestContentType: testData.get("rest.validation.contentType")[0], body: request.requestBodyCreateInvalidBasketAddressWithExtension)
     
        then: "check response"
            assert responseCode == 400
                                
        where:
            request << [new JSONRequest()]
    }
    
    @Ignore
    def "Create a Customer with Address"() {
        
        client.headers['Authorization'] = null
        client.headers['AuthToken'] = null
        
        when: "send request to server"  
            def createResponse = client.post(uri: baseURI + "customers", requestContentType: testData.get("rest.validation.contentType")[0], body: request.requestBodyCreateCustomerAddress)
        
        then: "check response"             
            assert createResponse.success
            assert createResponse.status == 201
            updateCustomerID request.customerNo
                            
        where:
            request << [new JSONRequest()]
    }
    
    
    def "Get Address list of Customer"() {
        
        client.headers['AuthToken'] = testData.get("rest.validation.authToken")[0]
        client.headers['Authorization'] = 'Basic ' + (user + ':' + password).bytes.encodeBase64().toString() 
             
        when: "send request to server"
            def createResponse = client.get(uri: baseURI + "customers/-/addresses", requestContentType: testData.get("rest.validation.contentType")[0])
        
        then: "check response"   
            assert createResponse.success
            assert createResponse.responseData.elements[0].title != null
            updateCustomerAddressURI createResponse.responseData.elements[0].uri
    }
    
    @Ignore
    def "Update Address of Customer"() {
    
        client.headers['AuthToken'] = testData.get("rest.validation.authToken")[0]
        client.headers['Authorization'] = 'Basic ' + (user + ':' + password).bytes.encodeBase64().toString() 
        
        when: "send request to server"        
            def createResponse = client.put(uri: baseUrl + "/INTERSHOP/rest/WFS/" + customerAddressURI, requestContentType: testData.get("rest.validation.contentType")[0], body: request.requestBodyUpdateCustomerAddress)
        
        then: "check response"
            assert createResponse.success
            assert createResponse.status == 200
        
        where:
            request << [new JSONRequest()]
    }
    
    @Ignore
    def "Create a SMB Customer with Address"() {
        
        def responseCode
        
        smbClient.handler.success = { responseReader ->
            responseCode = responseReader.status
         }
        
        smbClient.headers['Authorization'] = null
        smbClient.headers['AuthToken'] = null
        
        when: "send request to server"
            def createResponse = smbClient.post(uri: smbBaseURI + "customers", requestContentType: testData.get("rest.validation.contentType")[0], body: request.requestBodyCreateSMBCustomerAddress)
        
        then: "check response"
            assert createResponse == 201
                                        
        where:
            request << [new JSONRequest()]
    }
    

}