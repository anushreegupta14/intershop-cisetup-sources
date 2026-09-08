package geb.com.intershop.inspired.pages.backoffice.overview

import geb.Page

class CommerceManagementPage extends Page
{
    static url = "/INTERSHOP/web/WFS/inSPIRED-Site/en_US/-/USD/ViewApplication-DisplayWelcomePageKeepState"
    //Each page can define a way to check whether the underling browser is at the page that the Page class actually represents
    static at=
    {
         waitFor{$("body",class:"backoffice").size()>0} //The waitFor method verifies that the given clause returns a trueish value within a certain timeframe
    }
	
	static content=
	{
		intronicsChannelSelector {$("input",title:"inTRONICS").parents("li.application-type-intershop_B2CBackoffice.application-level-channel")}
	}
    
    def goToB2CChannel()
    {
		
		interact
        {
            moveToElement( $("button",class:"ui-dropdown ui-widget ui-state-default ui-corner-all channel-select-application"))
            click()
            moveToElement(intronicsChannelSelector)
            click()

        }
    }

}