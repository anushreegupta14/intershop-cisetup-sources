package geb.com.intershop.inspired.pages.backoffice.processchains

import geb.com.intershop.inspired.pages.backoffice.BackOfficePage

class ProcessChainListPage extends BackOfficePage
{
    static at =
    {
       waitFor(30, 10){ contentSlot.size()>0 }
    }
    
    static content= {
               
        contentSlot         { $('[data-testing-id^="page-bo-processchains-list"]')}
        
        newForm             { $("form", name:"ProcessChainForm") }
        
        btnNew(to: ProcessChainGeneralPage) { newForm.$('input', name: 'new') }
        btnRefresh          { newForm.$('input', name: 'refresh') }
        
    }
    
    def clickNew()
    {
        btnNew.click();
    }
    
    def openProcessChain(name)
    {
        $("a", href: contains("ViewProcessChain-Edit"), text: name).click();
    }
    
    def getLastExecutionDate(name)
    {
        return $('[data-testing-id="'+name+'"]').text().trim();
    }
    
    def refresh()
    {
        btnRefresh.click();
    }
    
    def sleepForNSeconds(int n)
    {
        Thread.sleep(n * 1000)
    }
    
}
