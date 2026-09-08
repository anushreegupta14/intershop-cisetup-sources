package geb.com.intershop.inspired.specs.storefront.b2c.rest

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.ContentType.XML

import geb.com.intershop.inspired.testdata.TestDataUsage
import geb.spock.GebReportingSpec
import groovyx.net.http.RESTClient
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Unroll

@Ignore("move to f_business or delete")
class WarrantyRESTSpec extends GebReportingSpec implements TestDataUsage {

    
        
    @Shared
    private def client = new RESTClient()

    @Shared
    def basketId
    
    @Shared
    def pliId

    @Unroll("REST API: Storefront warranty handling for ContentType #contentType")
    def "REST API: Storefront warranty handling"() {

        println("---> Running test with content type: " + contentType)
        
        def restUrl = baseUrl + testData.get("rest.base.url")[0];
 
        def user        =  testData.get("warranty.rest.user.login.eMail")[0]
        def password    =  testData.get("warranty.rest.user.login.password")[0]
        def productPath =  testData.get("warranty.rest.product.path")[0]
        def productSKU  =  testData.get("warranty.rest.product.sku")[0]
        def warrantySKU =  testData.get("warranty.rest.product.warranty.sku")[0]

        def credentials = user + ':' + password
                         
        client.setUri(restUrl)         
        client.headers['Authorization'] = 'Basic '+ credentials.bytes.encodeBase64().toString()       
        
        /*
         * GET product details
         * 
         * This could be done in the setup, no need to do this for XML and JSON twice
         */
        when: "I want to get a product with warranties"
      
            def productUrl = productPath + productSKU
            println(restUrl)
            println(productUrl)
            //execute the REST call to get the product              
            def product = client.get(uri: restUrl, path:productUrl, contentType: contentType)

            assert product.status == 200

            def productWarranties

            if (contentType == XML) {
                productWarranties = product.data.availableWarranties
            } else {
                productWarranties = product.data.availableWarranties
            }
        
        then: "I got a product with warranties"
            assert productWarranties != null
            
            def warrantyPrice
            if (contentType == XML) {
                warrantyPrice = productWarranties[0].availableWarranties[0].attributes.ResourceAttribute.value.value
            } else {
                warrantyPrice = productWarranties[0].attributes[0].value.value
            }
                        
            assert warrantyPrice != null
            
         /*
          * GET active basket
          */
          when: "I want to get the active basket"
               //TODO: can be done just once at the beginning
               def basketResponse = client.get(uri: restUrl, path: "baskets/-", contentType: contentType)
           
          then: "I got my active basket"     
               
              assert basketResponse.status == 200
              def basketId
              
              if(contentType == XML) {
                   basketId = basketResponse.data.id
              } else {
                  basketId = basketResponse.data.id
              }
               
           /*
            * POST a product (which has warranties)
            */
            when: "I want to add a product which has warranties"
                
                def productAddResponse
                
                //add with XML 
                if(contentType == XML){
                    
                    def productBody ="<?xml version=\"1.0\" encoding=\"utf-8\" ?>" + 
                                     "<LineItems>" + 
                                         "<elements>" +
                                             "<BasketLineItem>" +
                                                 "<sku>" + productSKU + "</sku>" + 
                                                 "<quantity type=\"Quantity\">" +
                                                     "<value>1</value>" + 
                                                 "</quantity>" + 
                                             "</BasketLineItem>" +
                                         "</elements>" + 
                                      "</LineItems>"
                                          
                    productAddResponse = client.post( path: 'baskets/' + basketId  + '/items',
                        body:productBody,
                        requestContentType: 'text/xml')
                }                           
                //add with JSON
                else{
                
                    productAddResponse = client.post( path: 'baskets/' + basketId  + '/items', 
                                        body : "{\"elements\":[{\"sku\":" + productSKU + ",\"quantity\":{\"value\": 1}}]}" , 
                                        contentType: contentType )                    
                }                    
                     
            then: "A product which has warranties has been added"
                assert productAddResponse.status == 201
            
            /*
             * GET basket items (to retrieve the product line item id)
             */
            when: "I want to get the pli id of the recent added product"
    
                def itemsResponse = client.get(uri: restUrl, path: 'baskets/' + basketId  + '/items', contentType: contentType)
                                
            then: "I got the pli id of the revent added product"
                assert itemsResponse.status == 200
                
                //it could be the wrong id when another product is already in the basket
                
                //XML response
                if(contentType == XML) {
                    pliId = itemsResponse.data.elements.BasketLineItem.id[0]
                //JSON response
                }else{
                    //strange the pli id contains square brackets like an array element
                    pliId = itemsResponse.data.elements.id[0]
                }
                assert pliId != null
                
            /*
             * POST the warranty product as a sub line item
             */
            when: "Add the warranty as sub line item"
            
                def warrantyAddResponse
                def warrantyAddPath = 'baskets/' + basketId  + '/items/' + pliId + '/sublineitems/warranty'

                if(contentType == XML){
                    def warrantyBody = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                                        "<BasketDependentLineItem name=\"dependentLineItem\" type=\"DependentLineItem\">" +
                                            "<sku>" + warrantySKU + "</sku>" +
                                        "</BasketDependentLineItem>"
                    warrantyAddResponse = client.put( path: warrantyAddPath,
                        body:warrantyBody,
                        requestContentType: 'text/xml')
                    
                }
                else {
                    warrantyAddResponse = client.put( path: warrantyAddPath,
                        body : "{\"sku\":\"" + warrantySKU + "\"}" ,
                        contentType: contentType )
                }
    
            then: "Warranty has been added"     
                assert warrantyAddResponse.status == 200
     
            //cleanup the individual line time, JSON only because it is not intended to be part of the test itself
            client.delete( path: 'baskets/' + basketId  + '/items/' + pliId,
                    requestContentType: JSON)

                       
        where:
            contentType << [JSON, XML]
    }
           
    def cleanupSpec() {
                
        //any other basket cleanup?                 
    }    
}