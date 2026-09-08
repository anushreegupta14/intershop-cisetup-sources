package geb.com.intershop.b2x.pages.backoffice.customer.segments

import geb.com.intershop.b2x.model.backoffice.customer.CustomerSegment
import geb.com.intershop.b2x.pages.backoffice.BackOfficePage

class CustomerSegmentPage extends BackOfficePage {

    static at = {
         waitFor { customerSegmentForm.size() > 0 && btnApply.size() > 0}
    }

    static content = {
        customerSegmentForm { $("form", name:"UserGroupForm") }
        nameField {customerSegmentForm.$("input", id: "UserGroupForm_Name")}
        idField {customerSegmentForm.$("input", id: "Dummy")}
        descriptionField {customerSegmentForm.$("textarea", id: "UserGroupForm_Description")}
        btnApply { customerSegmentForm.$("input", type : "submit", name: "update") }
        btnReset { customerSegmentForm.$("input", type : "submit", name: "reset") }
        btnDelete { customerSegmentForm.$("input", type : "submit", name: "confirmDelete") }
        btnBack(to: CustomerSegmentsListPage) { $("input", type : "submit", name: "back") }
    }

    void setCustomerSegmentData(CustomerSegment segment)
    {
        nameField.value segment.name
        if (segment.description != null)
        {
            descriptionField.text segment.description
        }
    }

    CustomerSegment getCustomerSegment()
    {
        new CustomerSegment(idField.value(),
                            nameField.value(),
                            descriptionField.value())
    }

    def clickApply()
    {
        btnApply.click()
    }

    def clickBack()
    {
        btnBack.click()
    }
}