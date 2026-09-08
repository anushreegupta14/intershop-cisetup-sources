package geb.com.intershop.inspired.pages.backoffice.product

import geb.com.intershop.inspired.pages.backoffice.catalog.CatalogSelectionPage
import geb.com.intershop.inspired.pages.backoffice.modules.ProductLinksListEntry

class ProductDetailsLinksPage extends ProductDetailsPage
{
    static at =
            {
                waitFor{ contentSlot.size()>0 }
            }

    static content= {

        contentSlot                 { $('table[data-testing-id="bo-product-links"]') }

        checkBoxWarranty            { $('input', id:"select_ES_Warranty_outgoing_product_links") }
        checkBoxIsWarrantyOf        { $('input', id:"select_ES_Warranty_incoming_product_links") }

        hasWarrantyForm             { $("form", name:"ES_Warranty_OutgoingProductLinks_Form") }
        btnAssignForHasWarranty(to: ProductsSelectionListPage)     { hasWarrantyForm.$('input', name: 'linkTypeSelected') }

        isWarrantyOfForm            { $("form", name:"ES_Warranty_IncomingProductLinks_Form") }
        btnAssignForWarrantyOf(to: ProductsSelectionListPage)      { isWarrantyOfForm.$('input', name: 'incomingLinkTypeSelected') }

        productLinksForHasWarranty  { $('div[id="ES_Warranty_OutgoingProductLinks"][class="isgrid-container selectable sticky-original"]').collect{ module ProductLinksListEntry, it } }
        productLinksForIsWarrantyOf { $('div[id="ES_Warranty_IncomingProductLinks"][class="isgrid-container selectable sticky-original"]').collect{ module ProductLinksListEntry, it } }

        checkBoxCrossSelling        { $('input', id:"select_ES_CrossSelling_outgoing_product_links") }
        hasCrosssellingForm             { $("form", name:"ES_CrossSelling_OutgoingProductLinks_Form") }
        btnAssignForHasCrossselling(to: ProductsSelectionListPage)     { hasCrosssellingForm.$('input', name: 'linkTypeSelected') }

        checkBoxAccessory       { $('input', id:"select_ES_Accessory_outgoing_product_links") }
        hasAccessoryForm             { $("form", name:"ES_Accessory_OutgoingProductLinks_Form") }
        hasAccessoryCategoryForm { $("form", name:"ES_Accessory_OutgoingCategoryLinks_Form") }
        btnAssignForHasAccessory(to: ProductsSelectionListPage)     { hasAccessoryForm.$('input', name: 'linkTypeSelected') }
        btnAssignForHasAccessoryCategory(to: CatalogSelectionPage) { hasAccessoryCategoryForm.$('input', name: 'categoryLinkTypeSelected') }

        checkBoxUpSelling       { $('input', id:"select_ES_UpSelling_outgoing_product_links") }
        hasUpSellingForm             { $("form", name:"ES_UpSelling_OutgoingProductLinks_Form") }
        hasUpSellingCategoryForm { $("form", name:"ES_UpSelling_OutgoingCategoryLinks_Form") }
        btnAssignForHasUpSelling(to: ProductsSelectionListPage)     { hasUpSellingForm.$('input', name: 'linkTypeSelected') }
        btnAssignForHasUpsellingCategory(to: CatalogSelectionPage) { hasUpSellingCategoryForm.$('input', name: 'categoryLinkTypeSelected') }
    }

    def setCheckBoxWarranty ()
    {
        checkBoxWarranty = true

        waitFor(10, 1){
            hasWarrantyForm.size()>0
        }

    }

    def unSetCheckBoxWarranty ()
    {
        checkBoxWarranty = false
    }

    def setCheckBoxIsWarrantyOf ()
    {
        checkBoxIsWarrantyOf = true

        waitFor(10, 1) {
            isWarrantyOfForm.size() > 0
        }
    }

    def unSetCheckBoxIsWarrantyOf ()
    {
        checkBoxIsWarrantyOf = false
    }

    def clickAssignForHasWarranty()
    {
        btnAssignForHasWarranty.click()

    }

    def clickAssignForWarrantyOf()
    {
        btnAssignForWarrantyOf.click()
    }

    /* CrossSelling */
    def clickAssignForHasCrossselling()
    {
        btnAssignForHasCrossselling.click()
    }

    def setCheckBoxCrossselling ()
    {
        checkBoxCrossSelling = true

        waitFor(10, 1){
            hasCrosssellingForm.size()>0
        }
    }

    def unSetCheckBoxCrossSelling ()
    {
        checkBoxCrossSelling = false
    }

    /* Accessory */
    def clickAssignForHasAccessory()
    {
        btnAssignForHasAccessory.click()
    }

    def clickAssignForHasAccessoryCategory()
    {
        btnAssignForHasAccessoryCategory.click()
    }

    def setCheckBoxAccessory ()
    {
        checkBoxAccessory = true

        waitFor(10, 1){
            hasAccessoryForm.size()>0
        }
    }

    def unSetCheckBoxAccessory ()
    {
        checkBoxAccessory = false
    }

    /* UpSelling */
    def clickAssignForHasUpSelling()
    {
        btnAssignForHasUpSelling.click()
    }

    def clickAssignForHasUpsellingCategory()
    {
        btnAssignForHasUpsellingCategory.click()
    }

    def setCheckBoxUpSelling ()
    {
        checkBoxUpSelling = true

        waitFor(10, 1){
            hasUpSellingForm.size()>0
        }
    }

    def unSetCheckBoxUpSelling ()
    {
        checkBoxUpselling = false
    }
}