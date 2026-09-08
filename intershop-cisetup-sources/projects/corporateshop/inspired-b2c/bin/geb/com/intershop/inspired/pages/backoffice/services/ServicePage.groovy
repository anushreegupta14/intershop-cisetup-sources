package geb.com.intershop.inspired.pages.backoffice.services

import geb.com.intershop.inspired.pages.backoffice.BackOfficePage

class ServicePage extends BackOfficePage
{
    static at = {
        waitFor{checkBoxServiceActive.size() > 0}
    }

    static content= {
//        serviceConfigurationTabsSection { $("table[data-testing-id='section-service-configuration-tabs']") }
        serviceConfigurationTabsSection { $("table td.table_tabs_dis_background") }
//        serviceConfigurationSharingTab  { $("td[data-testing-id='service-configuration-sharing-tab']") }

        checkBoxServiceActive  {$("input", id: "ServiceConfigurationAvailable", type: "checkbox")}
        serviceNameInput       {$("input", id: "ServiceConfigurationEditGeneralForm_ServiceConfigurationDisplayName")}
        applyButton            {$("input", name: "GeneralValidate")}
        resetButton            {$("input", name: "reset")}
        deleteButton           {$("input", name: "confirmDelete")}
    }

    def checkServiceActive()
    {
        checkBoxServiceActive = true
    }

    def uncheckServiceActive()
    {
        checkBoxServiceActive = false
    }

    def applyChanges()
    {
        applyButton.click()
        // Wait for success message to be shown
        waitFor{$("table", class:"notification").isDisplayed()}
    }

    String getServiceName()
    {
        serviceNameInput.value()
    }

    boolean isActive()
    {
        checkBoxServiceActive.value() == "available"
    }

    def openServiceSharingTab()
    {
//        def sharingLink = serviceConfigurationSharingTab.$("a")
        def sharingLink = serviceConfigurationTabsSection.$("a", class: "table_tabs_dis", text: "Sharing")

        sharingLink.click()
        // Wait for Service Sharing page to be loaded
        waitFor{ $("input", id: "unshared", type: "radio").isDisplayed() }
    }
}
