package geb.com.intershop.inspired.pages.backoffice.services

import geb.com.intershop.inspired.pages.backoffice.BackOfficePage

class BackOfficeServicesPage extends BackOfficePage {

    static at = {
        waitFor {sharedServicesSlot.size() > 0}
    }

    static content= {
        sharedServicesSlot  {$("table[data-testing-id='bo-channel-shared-services-overview']")}
        localServicesSlot {$("table[data-testing-id='bo-channel-local-services-overview']")}
        servicesContainer {$("div", "id": "main_content")}
    }

    def openLocalService(String serviceName)
    {
        def serviceLink = localServicesSlot.$("a", class: "table_detail_link", text: serviceName)
        assert serviceLink.size() > 0 : "Could not find link with text '" + serviceName + "'"
        serviceLink.click()
        // Wait for Service page to be loaded
        waitFor{$("input", id:"ServiceConfigurationAvailable", type: "checkbox")}
    }

    def openSharedService(String serviceName)
    {
        def serviceLink = sharedServicesSlot.$("a", class: "table_detail_link", text: serviceName).click()
        assert serviceLink.size() > 0 : "Could not find link with text '" + serviceName + "'"
        serviceLink.click()
        // Wait for Service page to be loaded
        waitFor{$("input", id:"ServiceConfigurationAvailable", type: "checkbox")}
    }
}
