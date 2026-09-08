package geb.com.intershop.b2x.pages.backoffice.customer.segments

import geb.com.intershop.b2x.pages.backoffice.BackOfficePage
import geb.com.intershop.b2x.model.backoffice.customer.CustomerSegment

class CustomerSegmentsListPage extends BackOfficePage {

    static at = {
         waitFor {customerSegmentsListForm.size()>0}
    }

    static content = {
        customerSegmentsListForm { $("form", name: "UserGroupListForm") }
        customerSegmentItems {$("table#UserGroupList tbody tr").tail()*.module(CustomerSegmentRow).collectEntries{[it.id, it]}} // tailing to skip the header row
        searchField { customerSegmentsListForm.$("input", type: "text", name: "SearchTerm") }
        btnFind(to : CustomerSegmentsListPage) { customerSegmentsListForm.$("input", type: "submit", name: "simpleSearch") }
        btnNew(to : NewCustomerSegmentPage) { $("input", type: "submit", name: "new") }
        btnDelete(to : DeleteCustomerSegmentsConfirmationPage) { $("input", type: "submit", name: "confirmDelete") }
    }

    NewCustomerSegmentPage clickNew()
    {
        btnNew.click()
        browser.page
    }

    DeleteCustomerSegmentsConfirmationPage clickDelete()
    {
        btnDelete.click()
        browser.page
    }

    void selectCustomerSegment(CustomerSegment customerSegment)
    {
        CustomerSegmentRow segmentRow = customerSegmentItems[customerSegment.id]
        if (segmentRow != null)
        {
            assert segmentRow.identicalTo(customerSegment)
            segmentRow.selectCustomerSegment()
        }
    }

    def searchSegment(segmentNameOrID)
    {
        searchField.value segmentNameOrID
        btnFind.click();

        waitFor{$("a", text: contains(segmentNameOrID)).size()>0}
    }

    void printCustomerSegments() {
        println "Customer Segments Count: " + customerSegmentItems.size()
        customerSegmentItems.values().each {segment -> println "Segment name: '${segment.name}', Segment ID: '${segment.id}'"}
    }

    boolean isPresent(CustomerSegment customerSegment)
    {
        customerSegmentItems.containsKey(customerSegment.id)
    }
}

