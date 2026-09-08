package geb.com.intershop.inspired.pages.backoffice.processchains

import geb.com.intershop.inspired.pages.backoffice.BackOfficePage

class ProcessChainStepsPage extends BackOfficePage
{
    static at =
    {
       waitFor(60, 10){ contentSlot.size()>0 }
    }
    
    static content= 
    {
        contentSlot         { $('[data-testing-id^="page-bo-processchains-steps"]')}
        
        processChainForm    { $("form", name:"ProcessChainStepsForm") }
        
        btnGroup            { processChainForm.$('input', name: 'group') }
        btnUnGroup          { processChainForm.$('input', name: 'ungroup') }
        btnMoveUp           { processChainForm.$('input', name: 'moveUp') }
        btnMoveDown         { processChainForm.$('input', name: 'moveDown') }
        btnApplyChange      { processChainForm.$('input', name: 'applyChange') }
        btnDelete           { processChainForm.$('input', name: 'delete') }
        btnAdd              { processChainForm.$('input', name: 'add') }
        btnType             { processChainForm.$('input', name: 'type') }
        
        domainInput         { processChainForm.$('select', name: 'NewStepForm_JobDomainUUID') }
        jobInput            { processChainForm.$('select', name: 'NewStepForm_JobName') }
        btnApplyJob         { processChainForm.$('input', name: 'apply') }
        steptype            { processChainForm.$('select', name: 'steptype') }
        
        pipelineInput       { processChainForm.$('input', name: 'AddPipelineForm_PipelineName') }
        startnodeInput      { processChainForm.$('input', name: 'AddPipelineForm_StartnodeName') }
        domainNameInput     { processChainForm.$('select', name: 'AddPipelineForm_DomainUUID') }
        btnApplyPipeline    { processChainForm.$('input', name: 'applyPipeline') }
        
        checkbox11          { processChainForm.$('input', name: 'SelectedStep', value: "1.1") }
        checkbox111         { processChainForm.$('input', name: 'SelectedStep', value: "1.1.1") }
        checkbox112         { processChainForm.$('input', name: 'SelectedStep', value: "1.1.2") }
        checkbox113         { processChainForm.$('input', name: 'SelectedStep', value: "1.1.3") }
        checkbox114         { processChainForm.$('input', name: 'SelectedStep', value: "1.1.4") }
        
        expectedType11      { processChainForm.$('select', name: 'ExpectedType_1.1') }
        expectedType111     { processChainForm.$('select', name: 'ExpectedType_1.1.1') }
        
    }
    
    def clickGroup()
    {
        btnGroup.click();
    }
    
    def clickUnGroup()
    {
        btnUnGroup.click();
    }
    
    def clickMoveUp()
    {
        btnMoveUp.click();
    }
    
    def clickMoveDown()
    {
        btnMoveDown.click();
    }
    
    private def clickAdd()
    {
        btnAdd.click();
    }
    
    private def setStepType(type)
    {
        steptype.find("option").find{
            it.text().trim() == type.trim()
        }.click()
    }
    
    private def clickType()
    {
        btnType.click();
    }
    
    def clickApplyChange()
    {
        btnApplyChange.click();
    }
    
    def clickDelete()
    {
        btnDelete.click();
    }
    
    def clickApplyJob()
    {
        btnApplyJob.click();
    }
    
    def clickApplyPipeline()
    {
        btnApplyPipeline.click();
    }
    
    def setCheckout11()
    {
        checkbox11 = true
    }
    
    def setCheckout111()
    {
        checkbox111 = true
    }
    
    def setCheckout112()
    {
        checkbox112 = true
    }
    
    def setCheckout113()
    {
        checkbox113 = true
    }
    
    def setCheckout114()
    {
        checkbox114 = true
    }
    
    def setExpectedType11(type)
    {
        expectedType11.find("option").find{
            it.text().trim() == type.trim()
        }.click()
    }
    
    def setExpectedType111(type)
    {
        expectedType111.find("option").find{
            it.text().trim() == type.trim()
        }.click()
    }
    
    def createJob(domain, jobname)
    {
        clickAdd()
        setStepType 'Job'
        clickType()
        setJobDomain domain
        setJobName jobname
        clickApplyJob()
    }
    
    def createPipeline(pipeline, startnode, domain)
    {
        clickAdd()
        setStepType 'Pipeline'
        clickType()
        setPipeline pipeline
        setStartnode startnode
        setDomainName domain
        clickApplyPipeline()
    }
    
    // private methods
    
    private def setJobDomain(domain)
    {
        domainInput.find("option").find{
            it.text().trim() == domain.trim()
        }.click()
    }
    
    private def setJobName(jobName)
    {
        jobInput.value jobName
    }
    
    private def setPipeline(pipeline)
    {
        pipelineInput.value pipeline
    }
    
    private def setStartnode(startnode)
    {
        startnodeInput.value startnode
    }
    
    private def setDomainName(domain)
    {
        domainNameInput.find("option").find{
            it.text().trim() == domain.trim()
        }.click()
    }
    
}
