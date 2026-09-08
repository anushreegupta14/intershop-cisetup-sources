package geb.com.intershop.inspired.pages.backoffice

import geb.Page
import geb.com.intershop.inspired.pages.backoffice.channels.BackOfficeChannelOverviewPage
import geb.com.intershop.inspired.pages.backoffice.preferences.BackOfficePreferencesPage

import geb.module.RadioButtons

class BackOfficePage extends Page
{
    static at=
    {
        waitFor(10) { $("body", "data-testing-id": "page-bo-base").size() > 0}
    }

    static content = {
        siteHeader(required: true)      { $('#main_header') }
        channelSelector(required: true) { $("button", class: "channel-select-application") }
        channel { $('input', 'name': 'dropdown_channel-select-application').module(RadioButtons) }
        logoutButton(required: true, to: BackOfficeLoginPage) { $("li",class:"logout").find("a") }
        homeButton(required: true, to: BackOfficePage) { $('a', 'id': 'brand_title') }
        preferences(required: true, to: BackOfficePreferencesPage) { $('a', 'data-testing-id': 'bo-sitenavbar-preferences') }
    }
    
    BackOfficeLoginPage logoutUser() {
        logoutButton.click()
        browser.page
    }
    
    BackOfficeChannelOverviewPage selectChannel(String channelName) {
        // open the Channel Select Menu
        channelSelector.click()
        // goto the channel name you wish to change to
        interact {
            moveToElement( $('input', 'title': channelName).closest("li") )
            click()
        }
		browser.page(BackOfficeChannelOverviewPage)
    }
    
	/**
     * Helper method to select a menu item from the top main navigation bar in the backoffice
	 * ASSUMPTION: hyperlinks in menu and menu item has "data-testing-id" HTML attribute set
     */
	void navigateToMainMenuItem(String menuId, String menuItemId) {
        interact
        {
            moveToElement( $("a", "data-testing-id": menuId))
            moveToElement( $("a", "data-testing-id": menuItemId))
            click()
        }
	}
    
    def sleepForNSeconds(int n)
    {
        Thread.sleep(n * 1000)
    }
    
    def setBasketMinTotalValue(String value){
        when: "set min item totals"
        waitFor{$("input",id:"ApplicationBasketPreference_BasketMinTotalValue_EUR").size()>0}
        $("input",id:"ApplicationBasketPreference_BasketMinTotalValue_EUR").value value
        $("input",id:"ApplicationBasketPreference_BasketMinTotalValue_USD").value value
        sleepForNSeconds(2)
        then: "...and click Apply."
        $("input",name:"update").click()
        
        then: "Done... Back to Test."
        waitFor{$("input",id:"ApplicationBasketPreference_BasketMinTotalValue_EUR").size()>0}
    }
    
    def toB2CSalesChannel()
    {
        when: "There is a dropdown at the upper right corner..."
        waitFor{$("body",class:"backoffice").size()>0}
        waitFor{$("button",class:"ui-dropdown ui-widget ui-state-default ui-corner-all channel-select-application")}

        then: "...where I choose the B2C sales channel."
        interact
        {
            moveToElement( $("button",class:"ui-dropdown ui-widget ui-state-default ui-corner-all channel-select-application"))
            click()
            waitFor{$("li",class:"application-type-intershop_B2CBackoffice application-level-channel ").size()>0}
            moveToElement($("li",class:"application-type-intershop_B2CBackoffice application-level-channel ",0))
            click()

        }
            
        then: "I wait for the correct side..."
        waitFor{$("table.wrapper").find("a",href:contains("ViewDomainPreference_52-Start")).size()>0}
                  
    }
}