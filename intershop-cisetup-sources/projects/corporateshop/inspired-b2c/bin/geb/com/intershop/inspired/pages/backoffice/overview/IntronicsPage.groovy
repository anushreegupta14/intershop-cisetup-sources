package geb.com.intershop.inspired.pages.backoffice.overview


import geb.Page

class IntronicsPage extends Page
{
    static at=
    {
      waitFor{$("body[data-testing-id='page-bo-base']").size()>0}
    }
    
    def goToPreferences()
    {
        $("a[data-testing-id='bo-sitenavbar-preferences']").click()
    }
    
    def goToApplications()
    {
        $("table[data-testing-id='bo-channel-overview']").find("a", text:"Applications").click()
    }
    
    def goToCatalogs()
    {
            $("div", id:"main_navigation").find("a", text:"Catalogs").click()
    }
}
