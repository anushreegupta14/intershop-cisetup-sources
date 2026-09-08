package geb.com.intershop.inspired.pages.backoffice.product

import geb.com.intershop.inspired.pages.backoffice.BackOfficePage

/**
 * This page describes only the outer html page details of every BO product handling page,
 * i.e. the  tabulator links and the 'Back to List' button.
 * All other product handling detail pages should inherit from this page.
 *
 */
class ProductDetailsPage extends BackOfficePage
{
    static at =
            {
                waitFor{ contentSlot.size()>0 }
            }

    static content= {
        contentSlot                 { $('table[data-testing-id^="page-bo-product-details"]') }

        linkGeneral                 { $("a",href:contains(~/ViewProduct.*-Edit/)) }
        linkContent                 { $("a",href:contains("ViewChannelProductContent-ListPageletEntryPoints")) }
        linkOnlineOffline           { $("a",href:contains(~/ViewProductMaintenance.*-Start/)) }
        linkAttributes              { $("a",href:contains(~/ViewProductCustomAttributes.*-ManageCA/)) }
        linkVariations              { $("a",href:contains(~/ViewProductVariations.*-View/)) }
        linkPricing(to: [ProductDetailsPricingWarrantyPage, ProductDetailsPricingPage])                 { $("a",href:contains(~/ViewProductPrices.*-View/)) }
        linkAuction                 { $("a",href:contains(~/ViewProductAuctionCategories.*-Start/)) }
        linkClassifications(to: ProductDetailsClassificationListPage)         { $("a",href:contains(~/ViewProductClassification.*-List/)) }
        linkAttachments             { $("a",href:contains(~/ViewProductAttachment.*-List/)) }
        linkLinks(to: ProductDetailsLinksPage)                   { $("a",href:contains(~/ViewProductLinks-Start/)) }
        linkBundles                 { $("a",href:contains(~/ViewProductBundle.*-Start/)) }
        linkRatings                 { $("a",href:contains(~/ViewChannelProductReviewList-ShowAll/)) }
        linkRetailSet               { $("a",href:contains(~/ViewProductRetailSet.*-Start/)) }
        linkChanges                 { $("a",href:contains("ViewChannelSharedProductChanges-Start")) }

        btnBackToList(to: [ProductDetailsClassificationListPage, ProductListPage])               { $('input', name: 'back') }
    }

    def clickGeneral()
    {
        linkGeneral.click()

    }

    def clickContent()
    {
        linkContent.click()

    }

    def clickOnlineOffline()
    {
        linkOnlineOffline.click()

    }

    def clickAttributes()
    {
        linkAttributes.click()

    }

    def clickVariations()
    {
        linkVariations.click()

    }

    def clickPricing()
    {
        linkPricing.click()

    }

    def clickAuction()
    {
        linkAuction.click()

    }

    def clickClassifications()
    {
        linkClassifications.click()
    }

    def clickAttachments()
    {
        linkAttachments.click()

    }

    def clickLinks()
    {
        linkLinks.click()

    }

    def clickBundles()
    {
        linkBundles.click()

    }

    def clickRatings()
    {
        linkRatings.click()

    }

    def clickRetailSet()
    {
        linkRetailSet.click()

    }

    def clickChanges()
    {
        linkChanges.click()

    }

    def clickBackToList()
    {
        btnBackToList.click()
    }

    def lockProduct()
    {
        $("img", src:endsWith("unlocked.gif")).click();
        waitFor(20,1) {$("img", src:endsWith("lockedForMe.gif")).displayed}
    }

    def unlockProduct()
    {
        $("img", src:endsWith("lockedForMe.gif")).click();
        waitFor(20,1) {$("img", src:endsWith("unlocked.gif")).displayed}
    }
}

