package geb.com.intershop.inspired.pages.backoffice.channels

import geb.com.intershop.inspired.pages.backoffice.BackOfficePage
import geb.com.intershop.inspired.pages.backoffice.modules.ChannelSiteNavigationBarModule
import geb.com.intershop.inspired.pages.backoffice.services.BackOfficeServicesPage

class BackOfficeChannelOverviewPage extends BackOfficePage
{    
    static at = 
    {
        waitFor(120) { $("div", "data-testing-id": "bo-channel-overview") }
    }

    static content = {
        // siteHeader { $('#main_header') }
        siteNavBar { module ChannelSiteNavigationBarModule }
        services(to: BackOfficeServicesPage) { $('a', 'data-testing-id': 'bo-sitenavbar-services') }
    }
    
    def goToPreferences()
    {
        siteNavBar.linkPreferences.click()
    }
    
    def goToCatalogs()
    {
        siteNavBar.linkCatalogs.click()
    }
    
    def goToApplications()
    {
        siteNavBar.linkApplications.click()
    }
}
