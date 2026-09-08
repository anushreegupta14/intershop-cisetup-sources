package geb.com.intershop.inspired.specs.backoffice

import geb.com.intershop.b2c.model.storefront.responsive.User
import geb.com.intershop.inspired.pages.backoffice.BackOfficeLoginPage
import geb.com.intershop.inspired.pages.backoffice.BackOfficePage
import geb.com.intershop.inspired.pages.backoffice.processchains.ProcessChainGeneralPage
import geb.com.intershop.inspired.pages.backoffice.processchains.ProcessChainListPage
import geb.com.intershop.inspired.pages.backoffice.processchains.ProcessChainStepsPage
import geb.spock.GebReportingSpec
import spock.lang.*

/**
 * Backoffice tests for process chain at inSPIRED.
 *
 *  Create a new process chain, executes and validates it
 *
 */
@Ignore("move to f_business or delete")
class ProcessChainSpec extends GebReportingSpec
{
    @Shared User b2cUser = new User("admin", "admin", "admin");
    @Shared String organization = "inSPIRED"
    
    @Shared String chainName = "TestChain" + System.currentTimeMillis().toString()
    
    /**
     * The test will create and execute a new process chain
     */
    def "Create process chain"()
    {
         given: "Logged in as admin.."
            to BackOfficeLoginPage
        when:
            login(b2cUser, "inSPIRED")
        then:
            at BackOfficePage
        when:
            navigateToMainMenuItem "bo-sitenavbar-masterdatatasks-organization", "link-massdatatasks-processchains-organization"

        then:
            at ProcessChainListPage
        
        when: "I click at the New button..."
        
        clickNew()
        
        at ProcessChainGeneralPage
        
        and: "I want to create a new process chain"
        
        createProcessChain(chainName, "test description")
        
        then:
        at ProcessChainStepsPage
        
        when: "I want to add some steps"
        
        createJob("inSPIRED-inTRONICS", "ProcessProductPriceRefresh")
        createPipeline("CorePrefix", "include", "inSPIRED-Site")
        createJob("inSPIRED-inTRONICS", "ProcessProductPriceRefresh")
        createPipeline("CorePrefix", "include", "inSPIRED-Site")
        
        and: "I want to change the execution type for the pipeline"
        
        setCheckout11()
        setExpectedType11 "SERIAL"
        clickApplyChange()
          
        and: "I want to group 2 steps"
        
        setCheckout113()
        setCheckout114()
        clickGroup()
        
        and: "I want to group the other 2 steps"
        
        setCheckout111()
        setCheckout112()
        clickGroup()
        
        and: "I want to execute 2 steps parallel" 
        
        setCheckout111()
        setExpectedType111 "PARALLEL"
        clickApplyChange()

        then:
        
        // check number of configured steps
        waitFor{$('[data-testing-id^="page-bo-processchains-singlestep"]').size() == 4}
        
        // check select boxes
        waitFor{$('select', name: contains('ExpectedType')).size() == 3}
        waitFor{$("select", name : 'ExpectedType_1.1').value() == 'Sequence'}
        waitFor{$("select", name : 'ExpectedType_1.1.1').value() == 'Sequence'}
        waitFor{$("select", name : 'ExpectedType_1.1.2').value() == 'Concurrent'}
        
        // check number of checkboxes
        waitFor{$('input', name: 'SelectedStep').size() == 8}
        
        and: "I want to access a process chain"
            at BackOfficePage
        when:
            navigateToMainMenuItem "bo-sitenavbar-masterdatatasks-organization", "link-massdatatasks-processchains-organization"
        then:
            at ProcessChainListPage
        when:
            openProcessChain(chainName)
        then:
            at ProcessChainGeneralPage

        when: "I want to executes a process chain"
            execute()
        and: "I check the last execution date of the process chain - if empty the process chain was not executed"
        navigateToMainMenuItem "bo-sitenavbar-masterdatatasks-organization", "link-massdatatasks-processchains-organization"
        then:
            at ProcessChainListPage
        when:
            sleepForNSeconds(10);
            refresh()
        
        at ProcessChainListPage

        waitFor{ $("a", "data-testing-id" : chainName).size() == 1}

        refresh()

        waitFor{ $("a", "data-testing-id" : chainName).size() == 1}

        def result = getLastExecutionDate(chainName)
        then:
            (result != null && !result.empty)

        when: "... process chain creation works"
        
            logoutUser()
        then:
            true
    }
}
