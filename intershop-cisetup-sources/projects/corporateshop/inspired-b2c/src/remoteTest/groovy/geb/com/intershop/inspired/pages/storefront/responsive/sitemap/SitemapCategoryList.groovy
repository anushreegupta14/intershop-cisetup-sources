package geb.com.intershop.inspired.pages.storefront.responsive.sitemap

import geb.com.intershop.inspired.pages.storefront.responsive.StorefrontPage

class SitemapCategoryListPage extends StorefrontPage
{
    static at =
    {
        waitFor{$('ul[data-testing-id="sitemap-category-list"]').displayed}
    }

}

