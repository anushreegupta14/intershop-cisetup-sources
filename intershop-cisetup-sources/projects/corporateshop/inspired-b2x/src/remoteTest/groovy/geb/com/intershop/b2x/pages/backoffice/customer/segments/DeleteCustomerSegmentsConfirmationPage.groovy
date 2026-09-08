package geb.com.intershop.b2x.pages.backoffice.customer.segments

import geb.com.intershop.b2x.pages.backoffice.BackOfficePage
import geb.com.intershop.b2x.model.backoffice.customer.CustomerSegment

class DeleteCustomerSegmentsConfirmationPage extends BackOfficePage {

    static at = {
         waitFor {confirmBox.size()>0}
    }

    static content = {
        confirmBox { $("table", class: "confirm_box") }
        customerSegmentsListForm { $("form", name: "UserGroupListForm") }
        customerSegmentItems {$("table#UserGroupList tbody tr").tail()*.module(CustomerSegmentRow).collectEntries{[it.id, it]}} // tailing to skip the header row
        btnNew(to : NewCustomerSegmentPage) { $("input", type: "submit", name: "new") }
        btnOK(to : CustomerSegmentsListPage) { $("input", type: "submit", name: "delete") }
        btnCancelDelete(to : CustomerSegmentsListPage) { $("input", type: "submit", name: "cancelDelete") }
    }

    NewCustomerSegmentPage clickNew()
    {
        btnNew.click()
        browser.page
    }

    CustomerSegmentsListPage clickOK()
    {
        btnOK.click()
        browser.page
    }

    CustomerSegmentsListPage clickCancel()
    {
        btnCancelDelete.click()
        browser.page
    }

    boolean isPresent(CustomerSegment customerSegment)
    {
        customerSegmentItems.containsKey(customerSegment.id)
    }
}

