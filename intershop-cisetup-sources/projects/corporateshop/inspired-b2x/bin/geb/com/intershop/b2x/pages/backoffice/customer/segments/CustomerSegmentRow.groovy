package geb.com.intershop.b2x.pages.backoffice.customer.segments

import geb.com.intershop.b2x.model.backoffice.customer.CustomerSegment

import geb.Module
import geb.navigator.Navigator
import geb.module.Checkbox

class CustomerSegmentRow extends Module
{
    static content =
    {
        cell { i -> $("td", i) }
        selectionCheckbox { cell(0).$("input", "name": "SelectedObjectUUID").module(Checkbox) }
        segmentDetailsLink(to: CustomerSegmentPage) { cell(1).$("a") }
        name { segmentDetailsLink.text() }
        id { cell(2).$("a").text() }
        description {cell(3).text()}
    }

    def openDetails()
    {
        segmentDetailsLink.click()
    }

    boolean identicalTo(CustomerSegment customerSegment)
    {
        customerSegment.name == this.name && customerSegment.id == this.id
    }

    def selectCustomerSegment()
    {
        selectionCheckbox.check()
    }

    def unselectCustomerSegment()
    {
        selectionCheckbox.uncheck()
    }

    boolean isSelected()
    {
        selectionCheckbox.checked
    }
}
