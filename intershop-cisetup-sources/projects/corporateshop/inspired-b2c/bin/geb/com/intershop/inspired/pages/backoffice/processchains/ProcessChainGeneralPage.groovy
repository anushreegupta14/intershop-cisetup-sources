package geb.com.intershop.inspired.pages.backoffice.processchains

import geb.com.intershop.inspired.pages.backoffice.BackOfficePage

class ProcessChainGeneralPage extends BackOfficePage
{
    static at =
    {
       waitFor(30, 10){ contentSlot.size()>0 }
    }

    static content= {
        
     contentSlot            { $('[data-testing-id="page-bo-processchains-general"]')}
        
     createForm             { $("form", name:"ProcessChainForm") }
     
     btnCreate              { createForm.$('input', name: 'create') }
     nameInput              { createForm.$('input', name: 'ProcessChainForm_Name')}
     descriptionInput       { createForm.$('textarea', name: 'ProcessChainForm_Description')}
     
     btnConfirmExecute      { createForm.$('input', name: 'confirmExecute') }
     btnExecute             { createForm.$('input', name: 'executeChain') }
    }
    
    def execute()
    {
        btnConfirmExecute.click();
        btnExecute.click();
    }
    
    def createProcessChain(name, description)
    {
        nameInput.value name
        descriptionInput.value description
        btnCreate.click();
    }
}
