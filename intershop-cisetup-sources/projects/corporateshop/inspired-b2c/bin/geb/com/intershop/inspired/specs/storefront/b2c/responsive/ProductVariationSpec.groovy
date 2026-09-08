package geb.com.intershop.inspired.specs.storefront.b2c.responsive


import geb.com.intershop.inspired.pages.storefront.responsive.HomePage
import geb.com.intershop.inspired.pages.storefront.responsive.shopping.ProductDetailPage
import geb.com.intershop.inspired.pages.storefront.responsive.shopping.SearchResultPage
import geb.com.intershop.inspired.testdata.TestDataUsage
import geb.spock.GebReportingSpec

class ProductVariationSpec extends GebReportingSpec implements TestDataUsage{
    def "Check default select attributes in mastered product"()
    {
        when: "I go to the homepage as customer"
            to HomePage
        and: "Search master product with variations"
            header.search(masterName)

        then: "I am at the product list page and see a mastered product"
            at SearchResultPage
            assert !$("[data-dynamic-block-product-sku=\""+masterSKU+"\"]").isEmpty()
        and: "Check default variation exists"
            assert $($('[data-dynamic-block-product-sku="'+masterSKU+'"]')[0]).find('input[name="SKU"]').value() == M7972533_DefaultVariation
        and: "Check changing variations after changing attributes"
            //black && 500 GB
            $($($('[data-dynamic-block-product-sku="'+masterSKU+'"]')[0]).find('select[name="VariationAttribute_Colour_of_product"]')[0]).click()
            $($($('[data-dynamic-block-product-sku="'+masterSKU+'"]')[0]).find('select[name="VariationAttribute_Colour_of_product"]')[0]).find("option").find{ it.value() == "Black" }.click()

            waitFor(10) {  $($('[data-dynamic-block-product-sku="'+masterSKU+'"]')[0]).find('input[value="'+M7972533_Black_500GB+'"]').size() > 0}
            //black && 1000 GB
            $($($('[data-dynamic-block-product-sku="'+masterSKU+'"]')[0]).find('select[name="VariationAttribute_Hard_disk_drive_capacity"]')[0]).click()
            $($($('[data-dynamic-block-product-sku="'+masterSKU+'"]')[0]).find('select[name="VariationAttribute_Hard_disk_drive_capacity"]')[0]).find("option").find{ it.value() == "1000 GB" }.click()
            waitFor(10) {  $($('[data-dynamic-block-product-sku="'+masterSKU+'"]')[0]).find('input[value="'+M7972533_Black_1000GB+'"]').size() > 0}
        then: "Go to product detail page"
            $($($('[data-dynamic-block-product-sku="'+masterSKU+'"]')[0]).find('.product-title')[0]).click()
                at ProductDetailPage
        and: "Check opened variation"
            //should be black && 1000 GB
            assert $($('.product-info')[0]).find('input[name="SKU"]').value() == M7972533_Black_1000GB
        and: "Change attribute and check new product"
            //black && 500 GB
            $($($('.product-info')[0]).find('select[name="VariationAttribute_Hard_disk_drive_capacity"]')[0]).click()
            $($($('.product-info')[0]).find('select[name="VariationAttribute_Hard_disk_drive_capacity"]')[0]).find("option").find{ it.value() == "500 GB" }.click()
            waitFor(10) {  $($('.product-info')[0]).find('input[value="'+M7972533_Black_500GB+'"]').size() > 0}
        where:
            masterSKU      = testData.get("product.master.defaultSelectionType.masterSKU")[0]
			masterName     = testData.get("product.master.defaultSelectionType.masterName")[0]
            M7972533_DefaultVariation      = testData.get("product.master.defaultSelectionType.M7972533.DefaultVariation")[0]
            M7972533_Black_500GB      = testData.get("product.master.defaultSelectionType.M7972533.Black_500GB")[0]
            M7972533_Black_1000GB      = testData.get("product.master.defaultSelectionType.M7972533.Black_1000GB")[0]
    }

    def "Check swatch image type attributes in mastered product"()
    {
        when: "I go to the homepage as unregistered customer"
            to HomePage
        and: "Search master product with variations"
            header.search(masterName)

        then: "I am at the product list page and see a mastered product"
            at SearchResultPage
            assert !$("[data-dynamic-block-product-sku=\""+masterSKU+"\"]").isEmpty()
        and: "Check default variation exists"
            assert $($('[data-dynamic-block-product-sku="'+masterSKU+'"]')[0]).find('input[name="SKU"]').value() == M4852589_DefaultVariation
        and: "Check changing variation after changing attribute"
            //blue
            $($($('[data-dynamic-block-product-sku="'+masterSKU+'"]')[0]).find('a[data-variation-product-attribute="Blue"]')[0]).click()
            waitFor(10) {  $($('[data-dynamic-block-product-sku="'+masterSKU+'"]')[0]).find('input[value="'+M4852589_BlueVariation+'"]').size() > 0}
        then: "Go to product detail page"
            $($($('[data-dynamic-block-product-sku="'+masterSKU+'"]')[0]).find('.product-title')[0]).click()
            at ProductDetailPage
        and: "Check opened variation"
            //should be blue
            assert $($('.product-info')[0]).find('input[name="SKU"]').value() == M4852589_BlueVariation
        and: "Change attribute and check new product"
            //change to default (Green)
            $($($('.product-info')[0]).find('a[data-variation-product-attribute="Green"]')[0]).click()
            waitFor(10) {   $($('.product-info')[0]).find('input[value="'+M4852589_DefaultVariation+'"]').size() > 0}
        where:
            masterSKU      = testData.get("product.master.swatchImageType.masterSKU")[0]
			masterName     = testData.get("product.master.swatchImageType.masterName")[0]
            M4852589_DefaultVariation      = testData.get("product.master.swatchImageType.M4852589.DefaultVariation")[0]
            M4852589_BlueVariation     = testData.get("product.master.swatchImageType.M4852589.BlueVariation")[0]
    }
}