package geb.com.intershop.inspired.pages.storefront.responsive.sitemap

import geb.com.intershop.inspired.pages.storefront.responsive.StorefrontPage

class SitemapPage extends StorefrontPage
{
    static at =
    {
        waitFor{$('div[data-testing-id="sitemap"]').size() > 0}
    }

}
