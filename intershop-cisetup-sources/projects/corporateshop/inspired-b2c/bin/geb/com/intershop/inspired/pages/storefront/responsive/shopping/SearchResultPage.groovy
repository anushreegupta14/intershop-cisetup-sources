package geb.com.intershop.inspired.pages.storefront.responsive.shopping

import geb.com.intershop.inspired.pages.storefront.responsive.StorefrontPage;
import geb.com.intershop.inspired.pages.storefront.responsive.modules.ProductTile

class SearchResultPage extends StorefrontPage
{
    static at =
    {
        waitFor{contentSlot.size()>0}
    }

    static content =
    {
        contentSlot    { $("div[data-testing-id='search-result-page']") }
        navigationBar  { contentSlot.$("div",class:"ish-pageNavigation-contents") }
        productTiles   { term -> module(new ProductTile(productTerm: term)) }
        listViewButton { $("a[data-testing-id='list-view-link']") }
        gridViewButton { $("a[data-testing-id='grid-view-link']") }
        contentResultRadio { $("input[data-switch-target-id='content-search-result']") }
        productResultRadio { $("input[data-switch-target-id='product-search-result']") }
    }
}
