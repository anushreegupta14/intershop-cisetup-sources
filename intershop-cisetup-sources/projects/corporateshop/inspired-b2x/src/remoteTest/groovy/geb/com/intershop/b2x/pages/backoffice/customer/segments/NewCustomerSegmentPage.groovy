package geb.com.intershop.b2x.pages.backoffice.customer.segments

import geb.com.intershop.b2x.pages.backoffice.BackOfficePage
import geb.com.intershop.b2x.model.backoffice.customer.CustomerSegment

class NewCustomerSegmentPage extends BackOfficePage {

    static at = {
         waitFor { newCustomerSegmentForm.size() > 0 && btnApply.size() > 0}
    }

    static content = {
        newCustomerSegmentForm { $("form", name:"UserGroupForm") }
        nameField {newCustomerSegmentForm.$("input", id: "UserGroupForm_Name")}
        idField {newCustomerSegmentForm.$("input", id: "UserGroupForm_UserGroupID")}
        descriptionField {newCustomerSegmentForm.$("textarea", id: "UserGroupForm_Description")}
        btnApply(to: CustomerSegmentPage) { $("input", type : "submit", name: "create") }
        btnCancel(to: CustomerSegmentsListPage) { $("input", type : "submit", name: "cancelCreate") }
    }

    void setCustomerSegmentData(CustomerSegment segment)
    {
        nameField.value segment.name
        idField.value segment.getIdWithoutPrefix()
        if (segment.description != null)
        {
            descriptionField.value segment.description
        }
    }

    def clickApply()
    {
        btnApply.click()
    }
}