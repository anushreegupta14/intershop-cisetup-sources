package geb.com.intershop.inspired.pages.backoffice.channels.reseller

import geb.com.intershop.inspired.pages.backoffice.catalog.CatalogListPage

class CatalogListResellerChannelPage extends CatalogListPage
{
    static at = {
        waitFor { $('[data-testing-id="bo-catalog-list-reseller"]').size() > 0 }
    }
}