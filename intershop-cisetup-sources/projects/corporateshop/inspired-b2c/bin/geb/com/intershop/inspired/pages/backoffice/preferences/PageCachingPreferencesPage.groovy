package geb.com.intershop.inspired.pages.backoffice.preferences

import geb.com.intershop.inspired.pages.backoffice.BOLoginPage

class PageCachingPreferencesPage extends BOLoginPage
{
    static at = {
        waitFor{ checkBoxPageCacheAllowed.size()>0 }
    }
    
    static content= {        
        checkBoxPageCacheAllowed  { $("input",name:"DomainPageCachingAllowed") }
        btnApply            { $('input', name: 'OK')}
    }
    
    def checkPageCachingAllowed()
    {
        checkBoxPageCacheAllowed = true
                
    }
    
    def uncheckPageCachingAllowed()
    {
        checkBoxPageCacheAllowed = false
                
    }
}
