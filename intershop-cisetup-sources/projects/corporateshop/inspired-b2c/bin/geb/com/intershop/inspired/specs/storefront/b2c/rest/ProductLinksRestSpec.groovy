/**
 *
 */
package geb.com.intershop.inspired.specs.storefront.b2c.rest

import org.apache.http.client.config.RequestConfig
import org.apache.http.config.SocketConfig
import org.apache.http.impl.client.HttpClients

import geb.com.intershop.inspired.pages.backoffice.BOLoginPage
import geb.com.intershop.inspired.pages.backoffice.product.ProductDetailsGeneralPage
import geb.com.intershop.inspired.pages.backoffice.product.ProductDetailsLinksPage
import geb.com.intershop.inspired.pages.backoffice.product.ProductListPage
import geb.com.intershop.inspired.pages.backoffice.product.ProductNewPage
import geb.com.intershop.inspired.testdata.TestDataUsage
import geb.spock.GebReportingSpec
import groovyx.net.http.RESTClient
import spock.lang.Shared

class ProductLinksRestSpec extends GebReportingSpec  implements TestDataUsage
{
    private static productLinksTestProducts = new Stack()

    static content= {
        linkLinks { $("a",href:contains(~/ViewCategoryLinks-Start/)) }
    }

    @Shared
    private RESTClient client = new RESTClient()
    private static String uri
    private static String sourceProductId
    private static String crossSellingProductId
    private static String accessoryProductId
    private static String upSellingProductId
    private static final String LINKS = "/links"
    private static final String CROSSSELLS = "/crosssells"

    def setupSpec() {
        setup:
        uri = baseUrl + testData.get("rest.base.url")[0] + "products/"
        client.headers['Content-Type'] = 'application/json'
        client.headers['Accept'] = 'application/json'

        SocketConfig sc = SocketConfig.custom().setSoTimeout(70000).build()
        RequestConfig rc = RequestConfig.custom().setConnectTimeout(70000).setSocketTimeout(70000).build()
        def hc = HttpClients.custom().setDefaultSocketConfig(sc).setDefaultRequestConfig(rc).build()
        client.client = hc

        to BOLoginPage

        at BOLoginPage
        login "admin","!InterShop00!","inSPIRED"

        toB2CSalesChannel()

        waitFor{$("a",href:contains("ViewOverview-ChannelCatalog")).size()>0 }
        $("a",href:contains("ViewOverview-ChannelCatalog"),0).click()

        waitFor{$("table.wrapper").find("a",text:contains("Products"),0).size()>0 }

        $("table.wrapper").find("a",text:contains("Products"),0).click()

        at ProductListPage

        // create source product
        sourceProductId = 'testProduct' + System.currentTimeMillis().toString()
        when: "I click at the New button..."
        clickNew()

        then: "...I'm at the 'new product' detail page."
        at ProductNewPage

        when: "I fill all my datas and submit...."
        setNameAndID sourceProductId , sourceProductId
        setOnline true
        setAvailable true
        btnApplyCreate.click()
        productLinksTestProducts.push (sourceProductId)

        then: "...I'm at the 'new product' detail page."
        at ProductDetailsGeneralPage

        when: "I press cancel button"
        clickBackToList()
        then: "I'm at the product list page"
        at ProductListPage

        // create crossSelling product
        crossSellingProductId = 'testProduct' + System.currentTimeMillis().toString()
        when: "I click at the New button..."
        clickNew()

        then: "...I'm at the 'new product' detail page."
        at ProductNewPage

        when: "I fill all my datas and submit...."
        setNameAndID crossSellingProductId , crossSellingProductId
        setOnline true
        setAvailable true
        btnApplyCreate.click()
        productLinksTestProducts.push (crossSellingProductId)

        then: "...I'm at the 'new product' detail page."
        at ProductDetailsGeneralPage

        when: "I press cancel button"
        clickBackToList()
        then: "I'm at the product list page"
        at ProductListPage

        // create accessory product
        accessoryProductId = 'testProduct' + System.currentTimeMillis().toString()
        when: "I click at the New button..."
        clickNew()

        then: "...I'm at the 'new product' detail page."
        at ProductNewPage

        when: "I fill all my datas and submit...."
        setNameAndID accessoryProductId , accessoryProductId
        setOnline true
        setAvailable true
        btnApplyCreate.click()
        productLinksTestProducts.push (accessoryProductId)

        then: "...I'm at the 'new product' detail page."
        at ProductDetailsGeneralPage

        when: "I press cancel button"
        clickBackToList()
        then: "I'm at the product list page"
        at ProductListPage

        // create upselling product
        upSellingProductId = 'testProduct' + System.currentTimeMillis().toString()
        when: "I click at the New button..."
        clickNew()

        then: "...I'm at the 'new product' detail page."
        at ProductNewPage

        when: "I fill all my datas and submit...."
        setNameAndID upSellingProductId , upSellingProductId
        setOnline true
        setAvailable true
        btnApplyCreate.click()
        productLinksTestProducts.push (upSellingProductId)

        then: "...I'm at the 'new product' detail page."
        at ProductDetailsGeneralPage

        when: "I press cancel button"
        clickBackToList()
        then: "I'm at the product list page"
        at ProductListPage

        when: "Select source product"
        searchProduct sourceProductId
        openProduct sourceProductId
        then: "...I'm at the source product detail page."
        at ProductDetailsGeneralPage

        when: "Go to links tab"
        linkLinks.click()

        /* Assign cross-selling product link */
        setCheckBoxCrossselling()
        clickAssignForHasCrossselling()
        findAndAcceptProduct crossSellingProductId

        /* Assign accessory product link */
        setCheckBoxAccessory()
        clickAssignForHasAccessory()
        findAndAcceptProduct accessoryProductId

        /* Assign upselling product link */
        setCheckBoxUpSelling()
        clickAssignForHasUpSelling()
        findAndAcceptProduct upSellingProductId

        /* Assign accessory category link */
        setCheckBoxAccessory()
        clickAssignForHasAccessoryCategory()
		waitFor(10, 1){
			catalogs.size()>0
		}
        catalogs.each({
            it.click()
        })
        clickOKBtn()

        /* Assign upselling category link */
        setCheckBoxUpSelling()
        clickAssignForHasUpsellingCategory()
		waitFor(10, 1){
			catalogs.size()>0
		}
        catalogs.each({
            it.click()
        })
        clickOKBtn()

        then: "I'm at the product links page"
        at ProductDetailsLinksPage
    }

    /**
     * Runs after all tests in this Spec run once.
     * Deletes all new created test warranties and products to enable multiple runs.
     */
    def cleanupSpec()
    {
        when:
        to BOLoginPage

        at BOLoginPage
        login "admin","!InterShop00!","inSPIRED"

        toB2CSalesChannel()

        waitFor{$("a",href:contains("ViewOverview-ChannelCatalog")).size()>0 }
        $("a",href:contains("ViewOverview-ChannelCatalog"),0).click()

        waitFor{$("table.wrapper").find("a",text:contains("Products"),0).size()>0 }

        $("table.wrapper").find("a",text:contains("Products"),0).click()

        then: "...go to the products list..."
        at ProductListPage

        and: "...and delete every new one."
        while ( !productLinksTestProducts.empty )
        {
            def sku = productLinksTestProducts.pop()
            deleteProduct sku
        }
    }

    def "Test get all product links (Content-Type: JSON)"() {
        when: "set accept header as json"
        client.headers['Accept'] = 'application/json'
        and: "send request to get links"
        def response = client.get(uri: uri + sourceProductId  + LINKS)
        then: "check response"
        assert response.success
        assert response.status == 200
        assert response.data['type'] == 'TypedLinks'

        def elements = response.data['elements']
        assert elements.size() > 0

        def foundAccessoryProdLink = false
        def foundUpsellingProdLink = false
        def foundAccessoryCatLink = false
        def foundUpsellingCatLink = false
        elements.each({
            def linkType = it['linkType']
            assert linkType != null
            if (linkType == 'accessory' || linkType == 'upselling')
            {
                assert it['productLinks'] != null
                assert it['productLinks'].size() > 0

                it['productLinks'].each({
                    if (linkType == 'accessory' && accessoryProductId == it['title'])
                    {
                        checkProductLinksAttrsJSON(it)
                        foundAccessoryProdLink = true
                    }
                    else if (linkType == 'upselling' && upSellingProductId == it['title'])
                    {
                        checkProductLinksAttrsJSON(it)
                        foundUpsellingProdLink = true
                    }
                })

                assert it['categoryLinks'] != null
                assert it['categoryLinks'].size() > 0

                it['categoryLinks'].each({
                    if (it['title'] == 'Cameras-Camcorders')
                    {
                        checkCategoryLinksAttrsJSON(it)
                        if (linkType == 'accessory')
                        {
                            foundAccessoryCatLink = true
                        }
                        else if (linkType == 'upselling')
                        {
                            foundUpsellingCatLink = true
                        }
                    }
                })
            }
        })

        assert foundAccessoryProdLink
        assert foundUpsellingProdLink
        assert foundAccessoryCatLink
        assert foundUpsellingCatLink
    }

    def "Test get product links (Content-Type: JSON)"() {
        when: "set accept header as json"
        client.headers['Accept'] = 'application/json'
        and: "send request to get links"
        def response = client.get(uri: uri + sourceProductId  + CROSSSELLS)
        then: "check response"
        assert response.success
        assert response.status == 200
        assert response.data['type'] == 'ResourceCollection'
        assert response.data['name'] == 'cross-sell-products'
        assert response.data['elements'].size() > 0
        getProductLinks(response.data['elements'])
    }

    def "Test product links with link type accessory (Content-Type: JSON)"()
    {
        when: "set accept header as json"
        client.headers['Accept'] = 'application/json'
        and: "send request to get links"
        def response = client.get(uri: uri + sourceProductId  + LINKS + '?linkType=accessory')
        then: "check response"
        assert response.success
        assert response.status == 200
        assert response.data['type'] == 'TypedLinks'
        def elements = response.data['elements']
        assert elements.size() > 0

        def foundAccessoryCatLink = false
        def foundAccessoryProdLink = false
        elements.each({
            assert it['linkType'] == 'accessory'
            assert it['productLinks'].size() > 0
            it['productLinks'].each({
                if (it['title'] == accessoryProductId)
                {
                    checkProductLinksAttrsJSON(it)
                    foundAccessoryProdLink = true
                }
            })

            assert it['categoryLinks'].size() > 0
            it['categoryLinks'].each({
                if (it['title'] == 'Cameras-Camcorders')
                {
                    checkCategoryLinksAttrsJSON(it)
                    foundAccessoryCatLink = true
                }
            })
        })

        assert foundAccessoryProdLink
        assert foundAccessoryCatLink
    }

    def "Test get product links with link type upselling (Content-Type: JSON)"()
    {
        when: "set accept header as json"
        client.headers['Accept'] = 'application/json'
        and: "send request to get links"
        def response = client.get(uri: uri + sourceProductId  + LINKS + '?linkType=upselling')
        then: "check response"
        assert response.success
        assert response.status == 200
        assert response.data['type'] == 'TypedLinks'
        def elements = response.data['elements']
        assert elements.size() > 0

        def foundUpsellingCatLink = false
        def foundUpsellingProdLink = false
        elements.each({
            assert it['linkType'] == 'upselling'
            assert it['productLinks'].size() > 0
            it['productLinks'].each({
                if (it['title'] == upSellingProductId)
                {
                    checkProductLinksAttrsJSON(it)
                    foundUpsellingProdLink = true
                }
            })

            assert it['categoryLinks'].size() > 0
            it['categoryLinks'].each({
                if (it['title'] == 'Cameras-Camcorders')
                {
                    checkCategoryLinksAttrsJSON(it)
                    foundUpsellingCatLink = true
                }
            })
        })

        assert foundUpsellingProdLink
        assert foundUpsellingCatLink
    }

    def "Test get product links with target object category (Content-Type: JSON)"()
    {
        when: "set accept header as json"
        client.headers['Accept'] = 'application/json'
        and: "send request to get links"
        def response = client.get(uri: uri + sourceProductId  + LINKS + '?targetObject=category')
        then: "check response"
        assert response.success
        assert response.status == 200
        assert response.data['type'] == 'TypedLinks'
        def elements = response.data['elements']
        assert elements.size() > 0

        def foundAccessoryCatLink = false
        def foundUpsellingCatLink = false
        elements.each({
            def linkType = it['linkType']
            if (linkType == 'accessory' || linkType == 'upselling')
            {
                assert it['categoryLinks'] != null
                it['categoryLinks'].each({
                    if (it['title'] == 'Cameras-Camcorders')
                    {
                        checkCategoryLinksAttrsJSON(it)
                        if (linkType == 'accessory')
                        {
                            foundAccessoryCatLink = true
                        }
                        else if (linkType == 'upselling')
                        {
                            foundUpsellingCatLink = true
                        }
                    }
                })
            }
        })
        assert foundAccessoryCatLink
        assert foundUpsellingCatLink
    }

    def "Test get product links with target object product (Content-Type: JSON)"()
    {
        when: "set accept header as json"
        client.headers['Accept'] = 'application/json'
        and: "send request to get links"
        def response = client.get(uri: uri + sourceProductId  + LINKS + '?targetObject=product')
        then: "check response"
        assert response.success
        assert response.status == 200
        assert response.data['type'] == 'TypedLinks'
        def elements = response.data['elements']
        assert elements.size() > 0

        def foundAccessoryProdLink = false
        def foundUpsellingProdLink = false
        elements.each({
            def linkType = it['linkType']
            if (linkType == 'accessory' || linkType == 'upselling')
            {
                assert it['productLinks'] != null
                it['productLinks'].each({
                    checkProductLinksAttrsJSON(it)
                    if (linkType == 'accessory' && it['title'] == accessoryProductId)
                    {
                        foundAccessoryProdLink = true
                    }
                    else if (linkType == 'upselling' && it['title'] == upSellingProductId)
                    {
                        foundUpsellingProdLink = true
                    }
                })
            }
        })
        assert foundAccessoryProdLink
        assert foundUpsellingProdLink
    }

    def "Get all product links (Content-Type: XML)"()
    {
        when: "set accept header as json"
        client.headers['Accept'] = 'text/xml'
        and: "send request to get links"
        def response = client.get(uri: uri + sourceProductId  + LINKS)
        then: "check response"
        assert response.success
        assert response.status == 200
        assert response.data.attributes()['type'] == 'TypedLinks'

        def elements = response.data['elements']
        assert elements.size() > 0

        elements.children().each({
            def linkType = it.linkType.text()
            if ('upselling' == linkType)
            {
                assert it.productLinks.productLinks.title.text() == upSellingProductId

                it.categoryLinks.categoryLinks.each({
                    if (it.title == 'Cameras-Camcorders')
                    {
                        assert it.uri != null && it.uri != ''
                        assert it.description != null && it.description != ''
                    }
                })
                def catLink = it.categoryLinks.categoryLinks.title.text()
                assert catLink.contains('Cameras-Camcorders') || catLink.contains('Computers') || catLink.contains('Home-Entertainment') || catLink.contains('Specials')
            }
            else if ('accessory' == linkType)
            {
                assert it.productLinks.productLinks.title.text() == accessoryProductId

                it.categoryLinks.categoryLinks.each({
                    if (it.title == 'Cameras-Camcorders')
                    {
                        assert it.uri != null && it.uri != ''
                        assert it.description != null && it.description != ''
                    }
                })
                def catLink = it.categoryLinks.categoryLinks.title.text()
                assert catLink.contains('Cameras-Camcorders') || catLink.contains('Computers') || catLink.contains('Home-Entertainment') || catLink.contains('Specials')
            }
            else if ('crossselling' == linkType)
            {
                assert it.productLinks.productLinks.title.text() == crossSellingProductId
            }
            assert it.productLinks.productLinks.uri.text() != null && it.productLinks.productLinks.uri.text() != ''
        })
    }

    def "Test get product links with target object category (Content-Type: XML)"()
    {
        when: "set accept header as json"
        client.headers['Accept'] = 'text/xml'
        and: "send request to get links"
        def response = client.get(uri: uri + sourceProductId  + LINKS + '?targetObject=category')
        then: "check response"
        assert response.success
        assert response.status == 200
        assert response.data.attributes()['type'] == 'TypedLinks'
        def elements = response.data['elements']
        assert elements.size() > 0

        def foundAccessoryCatLink = false
        def foundUpsellingCatLink = false
        elements.children().each({
            def linkType = it.linkType
            if (linkType.text() == 'accessory' || linkType.text() == 'upselling')
            {
                assert it.categoryLinks.categoryLinks != null
                it.categoryLinks.categoryLinks.each({
                    if (it.title.text() == 'Cameras-Camcorders')
                    {
                        checkCategoryLinksAttrsXML(it)
                        if(linkType == 'accessory')
                        {
                            foundAccessoryCatLink = true
                        }
                        else if (linkType == 'upselling')
                        {
                            foundUpsellingCatLink = true
                        }
                    }
                })
            }
        })

        assert foundAccessoryCatLink
        assert foundUpsellingCatLink
    }

    def "Test get product links with target object product (Content-Type: XML)"()
    {
        when: "set accept header as json"
        client.headers['Accept'] = 'text/xml'
        and: "send request to get links"
        def response = client.get(uri: uri + sourceProductId  + LINKS + '?targetObject=product')
        then: "check response"
        assert response.success
        assert response.status == 200
        assert response.data.attributes()['type'] == 'TypedLinks'
        def elements = response.data['elements']
        assert elements.size() > 0

        def foundAccessoryProdLink = false
        def foundUpsellingProdLink = false
        elements.children().each({
            def linkType = it.linkType
            if (linkType.text() == 'accessory' || linkType.text() == 'upselling')
            {
                assert it.productLink.productLink != null
                it.productLinks.productLinks.each({
                    checkProductLinksAttrsXML(it)
                    if(linkType == 'accessory' && it.title.text() == accessoryProductId)
                    {
                        foundAccessoryProdLink = true
                    }
                    else if (linkType == 'upselling' && it.title.text() == upSellingProductId)
                    {
                        foundUpsellingProdLink = true
                    }
                })
            }
        })
        assert foundAccessoryProdLink
        assert foundUpsellingProdLink
    }

    def "Test get product links with link Type accessory(Content-Type: XML)"()
    {
        when: "set accept header as json"
        client.headers['Accept'] = 'text/xml'
        and: "send request to get links"
        def response = client.get(uri: uri + sourceProductId  + LINKS + '?linkType=accessory')
        then: "check response"
        assert response.success
        assert response.status == 200
        assert response.data.attributes()['type'] == 'TypedLinks'

        def elements = response.data['elements']
        assert elements.size() > 0

        def foundAccessoryCatLink = false
        def foundAccessoryProdLink = false
        def linkType = elements[0].children().children().find{it.name() == 'linkType'}.text()
        assert linkType == 'accessory'

        def productLinks = elements[0].children().children().find{it.name() == 'productLinks'}
        assert productLinks != null

        if (productLinks.productLinks.title.text() == accessoryProductId)
        {
            checkProductLinksAttrsXML(productLinks.productLinks)
            foundAccessoryProdLink = true
        }

        def categoryLinks = elements[0].children().children().find{it.name() == 'categoryLinks'}
        assert categoryLinks != null
        categoryLinks.categoryLinks.each({
            if (it.title.text() == 'Cameras-Camcorders')
            {
                checkCategoryLinksAttrsXML(it)
                foundAccessoryCatLink = true
            }
        })

        assert foundAccessoryProdLink
        assert foundAccessoryCatLink
    }

    def "Test get product links with link Type upselling(Content-Type: XML)"()
    {
        when: "set accept header as json"
        client.headers['Accept'] = 'text/xml'
        and: "send request to get links"
        def response = client.get(uri: uri + sourceProductId  + LINKS + '?linkType=upselling')
        then: "check response"
        assert response.success
        assert response.status == 200
        assert response.data.attributes()['type'] == 'TypedLinks'

        def elements = response.data['elements']
        assert elements.size() > 0

        def foundUpsellingCatLink = false
        def foundUpsellingProdLink = false
        def linkType = elements[0].children().children().find{it.name() == 'linkType'}.text()
        assert linkType == 'upselling'

        def productLinks = elements[0].children().children().find{it.name() == 'productLinks'}
        assert productLinks != null

        if (productLinks.productLinks.title.text() == upSellingProductId)
        {
            checkProductLinksAttrsXML(productLinks.productLinks)
            foundUpsellingProdLink = true
        }

        def categoryLinks = elements[0].children().children().find{it.name() == 'categoryLinks'}
        assert categoryLinks != null
        categoryLinks.categoryLinks.each({
            if (it.title.text() == 'Cameras-Camcorders')
            {
                checkCategoryLinksAttrsXML(it)
                foundUpsellingCatLink = true
            }
        })

        assert foundUpsellingCatLink
        assert foundUpsellingProdLink
    }

    def private getProductLinks(elements)
    {
        elements.each({
            assert it['type'] != null
            assert it['uri'] != null
            assert it['title'] != null
            assert it['title'] == crossSellingProductId
        })
    }

    def private checkProductLinksAttrsJSON(productLink)
    {
        assert productLink['type'] == 'Link'
        assert productLink['title'] != null
        assert productLink['uri'] != null
    }

    def private checkProductLinksAttrsXML(productLink)
    {
        assert productLink[0].attributes()['type'] == 'Link'
        assert productLink.title.text() != null
        assert productLink.uri.text() != null
    }

    def private checkCategoryLinksAttrsJSON(categoryLink)
    {
        assert categoryLink['type'] == 'Link'
        assert categoryLink['title'] != null
        assert categoryLink['description'] != null
        assert categoryLink['uri'] != null

        def attr1 = categoryLink['attributes'][0]
        def attr2 = categoryLink['attributes'][1]
        if (attr1['name'] == 'hasOnlineProducts')
        {
            assert attr1['value'] == false
        }
        else if (attr1['name'] == 'hasOnlineSubCategories')
        {
            assert attr1['value'] == true
        }
        if (attr2['name'] == 'hasOnlineProducts') {
            assert attr2['value'] == false
        }
        else if (attr2['name'] == 'hasOnlineSubCategories')
        {
            assert attr1['value'] == true
        }
    }

    def private checkCategoryLinksAttrsXML(categoryLink)
    {
        assert categoryLink.attributes()['type'] == 'Link'
        assert categoryLink.title
        assert categoryLink.description != null
        assert  categoryLink.uri != null
        def attr1 = categoryLink.attributes[0].ResourceAttribute[0]
        def attr2 = categoryLink.attributes[0].ResourceAttribute[1]
        if (attr1.attributes()['name'] == 'hasOnlineProducts')
        {
            assert attr1.value.text() == 'false'
        }
        else if (attr1.attributes()['name'] == 'hasOnlineSubCategories')
        {
            assert attr1.value.text() == 'true'
        }
        if (attr2.attributes()['name'] == 'hasOnlineProducts')
        {
            assert attr2.value.text() == 'false'
        }
        else if (attr2.attributes()['name'] == 'hasOnlineSubCategories')
        {
            assert attr2.value.text() == 'true'
        }
    }
}
