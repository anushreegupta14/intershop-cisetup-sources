package geb.com.intershop.inspired.pages.storefront.responsive.sitemap

import geb.com.intershop.inspired.pages.storefront.responsive.StorefrontPage

class SitemapProductListPage extends StorefrontPage
{
    static at =
    {
        waitFor{$('ul[data-testing-id="sitemap-product-list"]').displayed}
    }

}

