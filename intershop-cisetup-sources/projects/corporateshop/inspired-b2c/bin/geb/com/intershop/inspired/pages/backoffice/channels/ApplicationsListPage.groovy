package geb.com.intershop.inspired.pages.backoffice.channels

import geb.com.intershop.inspired.pages.backoffice.application.ApplicationGeneralTabPage
import geb.com.intershop.inspired.pages.backoffice.modules.ChannelSiteNavigationBarModule

class ApplicationsListPage extends BackOfficeChannelOverviewPage
{
    static at =
    {
        waitFor{$('table[data-testing-id="bo-applicationslist-page"]').size()>0}
    }
    
    static content = 
    {
        siteNavBar {module ChannelSiteNavigationBarModule}
  
        linkShowAll (to: this) {$("input", type: "submit", name: "PageSize_-1")}
    }
    
    /**
     * Clicks the link of the application with the URL identifier {@code urlIdentifier}.
     * Browser will be sent to {@code ApplicationGeneralTabPage}.
     * 
     * @param urlIdentifier The URL identifier of the application.
     */
    def clickApplication(urlIdentifier)
    {
        $("table[data-testing-id='bo-applicationlist-table']")
            .$("td[data-testing-id='bo-applicationlist-urlidentifier-column']", text: urlIdentifier).previous().$("a")
            .click(ApplicationGeneralTabPage)
    }
    
    def clickOnApplicationName(name)
    {
        $("table[data-testing-id='bo-applicationlist-table']").$("a", text: name).click()
    }
}
