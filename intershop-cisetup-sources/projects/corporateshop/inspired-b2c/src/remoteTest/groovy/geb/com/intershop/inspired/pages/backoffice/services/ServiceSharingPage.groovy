package geb.com.intershop.inspired.pages.backoffice.services

import geb.com.intershop.inspired.pages.backoffice.BackOfficePage
import geb.module.RadioButtons

class ServiceSharingPage extends BackOfficePage
{
    static at = {
        waitFor { radioUnshared.size() > 0 }
    }

    static content = {
        sharingTypeRadioGroup   { $(name: "ServiceConfigurationEditScharingRuleForm_ServiceConfigurationSharingRule").module(RadioButtons) }

        radioUnshared           { $("input", id: "unshared", type: "radio") }
        radioSharedAndActive    { $("input", id: "sharedActivated", type: "radio") }
        radioSharedAndInactive  { $("input", id: "sharedDeactivated", type: "radio") }
        radioSharedAndMandatory { $("input", id: "sharedMandatory", type: "radio") }

        applyButton { $("input", name: "SharingRulesValidate", type: "submit") }
        resetButton { $("input", name: "sharingRules", type: "submit") }
    }

    def selectUnshared()
    {
        radioUnshared.click()
    }

    def selectSharedAndActive()
    {
        radioSharedAndActive.click()
    }

    def selectSharedAndInactive()
    {
        radioSharedAndInactive.click()
    }

    def selectSharedAndMandatory()
    {
        radioSharedAndMandatory.click()
    }

    boolean isSharedAndActive()
    {
        sharingTypeRadioGroup.checked == "sharedActivated"
    }

    def applyChanges()
    {
        applyButton.click()
        // Wait for success message to be shown
        waitFor { $("td", class:"notification")[0].isDisplayed() }
    }
}
