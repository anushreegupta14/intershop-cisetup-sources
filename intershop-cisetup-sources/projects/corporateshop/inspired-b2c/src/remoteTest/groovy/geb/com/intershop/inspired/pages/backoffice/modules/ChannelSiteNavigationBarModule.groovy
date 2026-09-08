package geb.com.intershop.inspired.pages.backoffice.modules

import geb.Module
import geb.com.intershop.inspired.pages.backoffice.catalog.CatalogOverviewPage
import geb.com.intershop.inspired.pages.backoffice.channels.ApplicationsListPage
import geb.com.intershop.inspired.pages.backoffice.channels.ChannelPreferencesPage

/**
 * Provides links of the channel navigation bar of the back office.
 *
 */
class ChannelSiteNavigationBarModule extends Module
{
    static content = 
    {
        linkPreferences (to: ChannelPreferencesPage) {$("a[data-testing-id='bo-sitenavbar-preferences']")}
        linkApplications (to: ApplicationsListPage) {$("a[data-testing-id='bo-sitenavbar-applications']")}
        linkCatalogs (to: CatalogOverviewPage) {$("a[data-testing-id='bo-sitenavbar-catalogs-channel']")}
    }
}
