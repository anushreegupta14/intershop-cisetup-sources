package geb.com.intershop.inspired.pages.backoffice.channels

import geb.Page
import geb.com.intershop.inspired.pages.backoffice.modules.ChannelSiteNavigationBarModule
import geb.com.intershop.inspired.pages.backoffice.preferences.PricingPreferencesPage

class ChannelPreferencesPage extends Page
{
    static at =
    {
        waitFor {contentSlot.size() > 0}
    }
    
    static content = 
    {
        siteNavBar {module ChannelSiteNavigationBarModule}
        
        contentSlot         {$("div[data-testing-id='bo-channel-preferences-overview']")}
        linkSEOSettings (to: PreferencesSEOSettingsPage) {$("a[data-testing-class='link-preferences-seo-channel']")}
        linkPricingSettings (to: PricingPreferencesPage) {contentSlot.$("a[data-testing-class='link-preferences-pricing-channel']")}
    }
    
    def goToPricingPreference()
    {
        linkPricingSettings.click()
    }
}
