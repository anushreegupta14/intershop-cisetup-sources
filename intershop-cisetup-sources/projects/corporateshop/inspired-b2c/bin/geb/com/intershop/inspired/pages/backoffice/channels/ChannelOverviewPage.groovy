package geb.com.intershop.inspired.pages.backoffice.channels

import geb.Page
import geb.com.intershop.inspired.pages.backoffice.modules.ChannelSiteNavigationBarModule

class ChannelOverviewPage extends Page
{
    static at =
    {
        waitFor {contentSlot.size() > 0}
    }
    
    static content = 
    {
        siteNavBar {module ChannelSiteNavigationBarModule}
        
        contentSlot         {$("div[data-testing-id='bo-channel-overview']")}
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