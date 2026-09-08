package geb.com.intershop.inspired.pages.storefront.responsive.shopping

import geb.com.intershop.inspired.pages.storefront.responsive.StorefrontPage;

class ComparePage extends StorefrontPage
{
    static at =
    {
        waitFor{$("div",class:"table-compare").size()>0}
    }

    static content =
    {
         productField {term -> $("a",class:"product-title",text:iContains(term))}
    }

}
