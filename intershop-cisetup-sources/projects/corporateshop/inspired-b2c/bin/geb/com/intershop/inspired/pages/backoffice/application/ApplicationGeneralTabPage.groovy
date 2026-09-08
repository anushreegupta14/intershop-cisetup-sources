package geb.com.intershop.inspired.pages.backoffice.application

import geb.com.intershop.inspired.pages.backoffice.channels.BackOfficeChannelOverviewPage;
import geb.com.intershop.inspired.pages.backoffice.modules.ChannelSiteNavigationBarModule

class ApplicationGeneralTabPage extends BackOfficeChannelOverviewPage
{
    static at =
    {
        waitFor {contentSlot.size() > 0}
    }
    
    static content = 
    {        
        contentSlot                 {$('form[data-testing-id="bo-application-general-page"]')}
        btnManageLocales            {contentSlot.$("input", name: "ManageLocales")}
        linkAddAllLanguages         {$("div", id: "LocalesMgmtDialog").$("a", text: contains('Add all'))}
        btnSelectLanguagesPopupOK   {$("div", id: "LocalesMgmtDialog").$("input", id: "LocalesMgmtDialog-ok")}
        btnApply                    {contentSlot.$("input", name: "Update")}
        shoppingCartTab             {$("a[data-testing-class='bo-application-cart-tab']")}
    }
    
    /**
     * Adds all available languages to the application.
     */
    def addAllAvailableLanguages()
    {
        btnManageLocales.click()
        linkAddAllLanguages.click()
        btnSelectLanguagesPopupOK.click()
        btnApply.click()
    }
    
    /**
     *  open shopping cart page 
     */
    def goToShoppingCartTab()
    {
        shoppingCartTab.click()
    }
}
