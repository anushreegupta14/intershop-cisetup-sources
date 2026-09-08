package geb.com.intershop.inspired.pages.backoffice.channels

import geb.Page
import geb.com.intershop.inspired.pages.backoffice.modules.ChannelSiteNavigationBarModule
import geb.module.Checkbox

class PreferencesSEOSettingsPage extends Page
{
    static at =
    {
        waitFor {SEOSettingsPreferencesForm.size() > 0}
    }
    
    static content = 
    {
        siteNavBar {module ChannelSiteNavigationBarModule}
        
        SEOSettingsPreferencesForm  {$('form[data-testing-id="bo-page-preferences-seo-channel"]')}
        
        checkboxRichSnippets        {$("input", id: "SEOSettingsPreferences_RichSnippetsEnabled")}
        checkboxHRefLangLinks       {$("input", id: "SEOSettingsPreferences_HRefLangEnabled")}
        checkboxURLRewriting        {$("input", id: "SEOSettingsPreferences_URLRewriteEnabled")}
        
        inputRewriteSiteName        {$("input", id: "SEOSettingsPreferences_URLRewriteSiteName")}
        
        btnApply                    {$("input", name: "updateSettings")}
        btnReset                    {$("input", name: "resetSettings")}
    }
}
