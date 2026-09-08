package geb.com.intershop.inspired.pages.storefront.responsive.shopping

import geb.com.intershop.inspired.pages.storefront.responsive.StorefrontPage;

import geb.Page

class NoSearchResultPage extends StorefrontPage
{

    static at =
    {
        waitFor{noResultField.size()>0}
    }

    static content =
    {
        contentSlot {$("div",role:"main")}
        noResultField {contentSlot.$("div[data-testing-id='no-search-result-page']")}
    }
}
