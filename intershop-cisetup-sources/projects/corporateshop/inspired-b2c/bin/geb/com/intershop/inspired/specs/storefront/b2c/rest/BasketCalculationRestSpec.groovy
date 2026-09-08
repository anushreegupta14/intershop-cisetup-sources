package geb.com.intershop.inspired.specs.storefront.b2c.rest

import static groovyx.net.http.ContentType.JSON

import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext

import org.apache.http.client.HttpClient
import org.apache.http.client.config.RequestConfig
import org.apache.http.config.SocketConfig
import org.apache.http.impl.client.HttpClients
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.apache.http.conn.ssl.TrustAllStrategy
import org.apache.http.ssl.SSLContextBuilder

import geb.com.intershop.inspired.specs.storefront.b2c.rest.*
import geb.com.intershop.inspired.pages.backoffice.*
import geb.com.intershop.inspired.pages.backoffice.application.*
import geb.com.intershop.inspired.pages.backoffice.catalog.product.*
import geb.com.intershop.inspired.pages.backoffice.channels.*
import geb.com.intershop.inspired.pages.backoffice.overview.*
import geb.com.intershop.inspired.pages.backoffice.preferences.*
import geb.com.intershop.inspired.pages.storefront.*
import geb.com.intershop.inspired.pages.storefront.responsive.*
import geb.com.intershop.inspired.pages.storefront.responsive.checkout.*
import geb.com.intershop.inspired.testdata.TestDataUsage
import geb.spock.GebReportingSpec
import groovyx.net.http.RESTClient
import spock.lang.*

/*
 * =====================================================================
 * Applies the test cases described in


 * BasketCalculation/SimpleTestCases/Rest_Tests/V3_MultiLineItemTest.xlsx
 * =====================================================================
 */
class BasketCalculationRestSpec extends GebReportingSpec implements TestDataUsage
{
    @Shared
    RESTClient client = createRestClient()

    private static String resource, password, mail, customer, organization, adminPassword, username, uri

    private static productID1, productID2, productID3

    private Object response

    private final static String resourceBaskets = "baskets"
    private final static String resourceOrders = "orders"

    //JSON Strings
    private static String commonShippingAddressJson, basketJson, invoiceAddressJson, itemsJson, paymentJson, shipToAddressJson

    private RESTClient createRestClient() {

        CloseableHttpClient httpClient = createHttpClient()

        RESTClient client = new LoggingRESTClient();
        client.setClient(httpClient);

        client.handler.failure = client.handler.success;

        return client;
    }

    private CloseableHttpClient createHttpClient() {
        SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(70000).build();
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(70000).setSocketTimeout(70000).build();
        SSLContext sslContext = SSLContextBuilder.create().loadTrustMaterial(new TrustAllStrategy()).build();
        HostnameVerifier hostnameVerifier = new NoopHostnameVerifier();
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);

        HttpClient httpClient = HttpClients
                .custom()
                .setSSLSocketFactory(sslSocketFactory)
                .setDefaultSocketConfig(socketConfig)
                .setDefaultRequestConfig(requestConfig)
                .build();
        httpClient
    }

    def setupSpec()
    {
        setup:
        customer                = testData.get("rest.calculation.email")[0]
        password                = testData.get("rest.calculation.password")[0]
        resource                = testData.get("rest.calculation.resource")[0]

        uri                     = baseUrl + testData.get("rest.base.url")[0]

        productID1              = testData.get("rest.calculation.product.sku")[0]
        productID2              = testData.get("rest.calculation.product.sku")[1]
        productID3              = testData.get("rest.calculation.product.sku")[4]

        username                = "admin"
        adminPassword           = "!InterShop00!"
        organization            = "inSPIRED"

        def credentials = customer + ':' + password
        client.headers['Authorization'] = 'Basic '+ credentials.bytes.encodeBase64().toString() 
		client.headers['Content-Type'] = 'application/json'
        client.headers['Accept'] = 'application/json'

        SocketConfig sc = SocketConfig.custom().setSoTimeout(70000).build()
        RequestConfig rc = RequestConfig.custom().setConnectTimeout(70000).setSocketTimeout(70000).build()
        def hc = HttpClients.custom().setDefaultSocketConfig(sc).setDefaultRequestConfig(rc).build()
        client.client = hc
    }


    /**
     * Runs after all tests in this Spec run once.
     */
    def cleanupSpec()
    {
        to BOLoginPage
        
        at BOLoginPage
        login username, adminPassword, organization
        
        at CommerceManagementPage
        goToB2CChannel()
        
        at BackOfficeChannelOverviewPage
        goToPreferences()

        at ChannelPreferencesPage
        goToPricingPreference()

        at PricingPreferencesPage
        applyDefault()
   }
    
    /*
     * Tests the result under the "Price Display for Individual Customers: Net" option
     * 
     * should resolved by IS-16050
     */
	
    def "Test PriceType Net / PriceDisplay Net Calculations"()
    {
       /* Set "Price Display for Individual Customers" to "Net"*/
       setPreferencesInCommerceManagement(true, true)
        
       /*=====================================================================
       * Test the calculations using REST
       * =====================================================================
       */
        
       given: "Create order via REST" 
        
       //get basket
       response = client.post(uri: uri + resourceBaskets, requestContentType: JSON)
       String basketUri = response.data.get("uri").toString()
       String resourceBasketId = basketUri.substring(basketUri.lastIndexOf("/")) //e.g. /3yAKAM7CzJ8AAAFUOw8baOx9
       String authenticationToken =  response.getHeaders()["authentication-token"].getValue()
       client.headers['authentication-token'] = authenticationToken
       
       // set line items
       setItemsBodyMultipleProducts(productID1)
       client.post(uri: uri + resourceBaskets + resourceBasketId + "/items", requestContentType: JSON, body: itemsJson)
       
       // set payment method
       setPaymentBody()
       client.post(uri: uri + resourceBaskets + resourceBasketId + "/payments", requestContentType: JSON, body: paymentJson)
       
       // set common ship to address on line items
       setShipToAddress()
       response = client.put(uri: uri + resourceBaskets + resourceBasketId, requestContentType: JSON, body: shipToAddressJson)
              
       // submit order
       setBasketJson(resourceBasketId.substring(1))
       response = client.post(uri: uri + resourceOrders, requestContentType: JSON, body: basketJson)

       when: "Get order info"
       String orderUrl = response.data['uri']
       String resourceOrderId = orderUrl.substring(orderUrl.lastIndexOf("/"))
       response = client.get(uri: uri + resourceOrders + resourceOrderId, requestContentType: JSON)

       then: "Output"
       assert response.status == 200 : "Error getting order info"
       assert response.data['totals']['itemTotal']['value'] == 86.82 //The net-net total
    }
    
    /*
     * Tests the result under the "Price Display for Individual Customers: Gross" option (default)"
     *
     * should resolved by using calculation rule set v3
     */

    def "Test PriceType Net / PriceDisplay Gross Calculations"()
    {
		/* Set "Price Display for Individual Customers" to "Gross"*/
		setPreferencesInCommerceManagement(true, false)
		
	   /*=====================================================================
       * Test the calculations using REST
       * =====================================================================
       */
	   
        given: "Create order via REST"
        
        //get basket
        response = client.post(uri: uri + resourceBaskets, requestContentType: JSON)
        String basketUri = response.data.get("uri").toString()
        String resourceBasketId = basketUri.substring(basketUri.lastIndexOf("/")) //e.g. /3yAKAM7CzJ8AAAFUOw8baOx9
        String authenticationToken =  response.getHeaders()["authentication-token"].getValue()
        client.headers['authentication-token'] = authenticationToken
        
        // set line items
        setItemsBodyMultipleProducts(productID3)
        client.post(uri: uri + resourceBaskets + resourceBasketId + "/items", requestContentType: JSON, body: itemsJson)
        
        // set payment method
        setPaymentBody()
        client.post(uri: uri + resourceBaskets + resourceBasketId + "/payments", requestContentType: JSON, body: paymentJson)
        
        // set common ship to address on line items
        setShipToAddress()
        client.put(uri: uri + resourceBaskets + resourceBasketId, requestContentType: JSON, body: shipToAddressJson)
                
        // submit order
        setBasketJson(resourceBasketId.substring(1))
        response = client.post(uri: uri + resourceOrders, requestContentType: JSON, body: basketJson)
        
        when: "Get order info"
        String orderUrl = response.data['uri']
        String resourceOrderId = orderUrl.substring(orderUrl.lastIndexOf("/"))
        response = client.get(uri: uri + resourceOrders + resourceOrderId, requestContentType: JSON)

        then: "Output"
        assert response.status == 200 : "Error getting order info"
        assert response.data['totals']['itemTotal']['value'] == 124.74 //The net-gross total
    }
    
    
    /*
     * Tests the result under the "Price Display for Individual Customers: Gross" option (default)"
     *
     * should resolved by using calculation rule set v3
     */

    def "Test PriceType Gross / PriceDisplay Gross Calculations"()
    {
		/* Set "Price Display for Individual Customers" to "Gross"*/
		setPreferencesInCommerceManagement(false, false)
		
	   /*=====================================================================
       * Test the calculations using REST
       * =====================================================================
       */
	   
        given: "Create order via REST"
        
        //get basket
        response = client.post(uri: uri + resourceBaskets, requestContentType: JSON)
        String basketUri = response.data.get("uri").toString()
        String resourceBasketId = basketUri.substring(basketUri.lastIndexOf("/")) //e.g. /3yAKAM7CzJ8AAAFUOw8baOx9
        String authenticationToken =  response.getHeaders()["authentication-token"].getValue()
        client.headers['authentication-token'] = authenticationToken
        
        // set line items
        setItemsBodyMultipleProducts(productID3)
        client.post(uri: uri + resourceBaskets + resourceBasketId + "/items", requestContentType: JSON, body: itemsJson)
        
        // set payment method
        setPaymentBody()
        client.post(uri: uri + resourceBaskets + resourceBasketId + "/payments", requestContentType: JSON, body: paymentJson)
        
        // set common ship to address on line items
        setShipToAddress()
        client.put(uri: uri + resourceBaskets + resourceBasketId, requestContentType: JSON, body: shipToAddressJson)
                
        // submit order
        setBasketJson(resourceBasketId.substring(1))
        response = client.post(uri: uri + resourceOrders, requestContentType: JSON, body: basketJson)
        
        when: "Get order info"
        String orderUrl = response.data['uri']
        String resourceOrderId = orderUrl.substring(orderUrl.lastIndexOf("/"))
        response = client.get(uri: uri + resourceOrders + resourceOrderId, requestContentType: JSON)

        then: "Output"
        assert response.status == 200 : "Error getting order info"
        assert response.data['totals']['itemTotal']['value'] == 104.82 //The gross-gross total
    }
    
    /******************************Auxiliary methods that set the static JSON String variables******************************/
    
    /*
    * =====================================================================
    * Prepares a multi-line-item order
    *=====================================================================
    */
    def setItemsBodyMultipleProducts = {
        
        product ->
    
        itemsJson = '''{
        "elements":
        [
            {
                "sku": ''' + productID2 + ''',
                "quantity": { "value": 3 }
            },
            {
                "sku":''' + product + ''',
                "quantity": { "value": 3}
            }
        ]
        }'''
    }
    
    /*
    *=====================================================================
    * Prepares a payment method
    * =====================================================================
    */
    def setPaymentBody = {
        paymentJson = '''
        {
            "name":"ISH_DEBIT_TRANSFER",
            "type":"Payment",
            "parameters":[
             {
                "key":"BIC",
                "property":"12345678901"
             },
             {
                "key":"IBAN",
                "property":"123456789012345678"
             },
             {
                "key":"holder",
                "property":"Patricia Miller"
            }
            ]
        }
        '''
    }
    
    
    /*
    * =====================================================================
    * Prepares the shipping address for a specific line item
    * =====================================================================
    */
    def setShipToAddress = {
    
        def tempBuilder = new groovy.json.JsonBuilder()
    
        tempBuilder
        {
            commonShipToAddress {
                    type "Address"
                    country "Germany"
                    city "Potsdam"
                    postalCode "14482"
                    street "Berliner Str. 20"
                    phoneHome "049364112677"
                    firstName "Patricia"
                    lastName "Miller"
                    title "Mrs."
                    addressName "Patricia Miller, Berliner Str. 20, Potsdam"
                    countryCode "DE"
            }
        }
    
        shipToAddressJson = tempBuilder.toString()
    
    }
    
    /*
     * =====================================================================
     * set basket for order
     * =====================================================================
     */
    def setBasketJson = { String id ->
        basketJson = '''{"basketID":"''' + id + '''","acceptTermsAndConditions": "true"}'''
    }
    
    /*
    * =====================================================================
    * Set Net pricing preference in commerce management
    * =====================================================================
    */
    def setPreferencesInCommerceManagement(boolean priceTypeNet, boolean priceDisplayNet)
    {
        to BOLoginPage
        
        at BOLoginPage
        login username, adminPassword, organization
        
        at CommerceManagementPage
        goToB2CChannel()
        
        at BackOfficeChannelOverviewPage
        goToPreferences()

        at ChannelPreferencesPage
        goToPricingPreference()

        at PricingPreferencesPage
        
        def priceType = "gross";
        def priceDisplay = "gross";
        
        if (priceTypeNet == true) 
        {
           priceType = "net";
        }

        if (priceDisplayNet == true) 
        {
           priceDisplay = "net";
        }
        applyPriceTypeAndDisplay(priceType, priceDisplay);
    }
}