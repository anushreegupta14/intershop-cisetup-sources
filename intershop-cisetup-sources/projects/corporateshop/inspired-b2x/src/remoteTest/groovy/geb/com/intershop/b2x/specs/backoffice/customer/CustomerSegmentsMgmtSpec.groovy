package geb.com.intershop.b2x.specs.backoffice.customer

import geb.com.intershop.b2x.model.storefront.responsive.User
import geb.com.intershop.b2x.model.backoffice.customer.CustomerSegment

import geb.com.intershop.b2x.pages.backoffice.authentication.BackOfficeAuthentication
import geb.com.intershop.b2x.pages.backoffice.channel.BackOfficeChannelOverviewPage
import geb.com.intershop.b2x.pages.backoffice.customer.segments.*

import geb.com.intershop.b2x.testdata.TestDataUsage

import geb.spock.GebReportingSpec
import spock.lang.Shared
import spock.lang.Stepwise

@Stepwise
class CustomerSegmentsMgmtSpec extends GebReportingSpec implements BackOfficeAuthentication, TestDataUsage
{
    @Shared
    private String organizationName = getTestValue("backoffice.organization.name")

    @Shared
    private String channelName = getTestValue("backoffice.channel.name")

    @Shared
    private User backofficeAdminUser = new User(getTestValue("backoffice.user.login"), "first", "last")

    @Shared
    private List<CustomerSegment> customerSegments = generateCustomerSegments()

    def setupSpec()
    {
        setup:
        given: "Logged in as admin.."
            logInUser(backofficeAdminUser, organizationName)
        when: "I go to the Channel"
            selectChannel(channelName)
            at BackOfficeChannelOverviewPage
        and: "Select Customer Segments Link"
            navigateToMainMenuItem "bo-sitenavbar-customers", "link-customers-customer-segments"
        then: "I'm at the CustomerSegmentsListPage."
            at CustomerSegmentsListPage
    }

    def "Create New Customer Segment"() {
        
        given: "on Customer Segments page"
            at CustomerSegmentsListPage
            assert !isPresent(customerSegment)
        when: "... Click create new segment."
            clickNew()
        and: "Set Customer Segment data"
            at NewCustomerSegmentPage
            setCustomerSegmentData(customerSegment)
        and : "Save the changes"
            clickApply()
        then: "I'm on Customer Segment page"
            at CustomerSegmentPage
            getCustomerSegment().id == customerSegment.id
        when : "Go back to Customer Segments Page"
            clickBack()
        then: "Created Customer Segment is available in the list"
            at CustomerSegmentsListPage
            isPresent(customerSegment)
        where:
            customerSegment << customerSegments
    }

    def "Delete Customer Segment"() {
        
        given: "on Customer Segments page"
            at CustomerSegmentsListPage
            //searchSegment(customerSegment.id)
            assert isPresent(customerSegment)
        when: "... Select customer segment."
            selectCustomerSegment(customerSegment)
        and: "Click on Delete button"
            clickDelete()
        then: "I'm on Delete Customer Segment Confirmation page"
            at DeleteCustomerSegmentsConfirmationPage
        when : "Confirm Customer Segment Deletion"
            clickOK()
        then: "Customer Segment is no more available in the list"
            at CustomerSegmentsListPage
            !isPresent(customerSegment)
        where:
            customerSegment << customerSegments
    }

    def List<CustomerSegment> generateCustomerSegments()
    {
        def segmentsList = []
        def segmentsCount = 1..1
        int randomSuffux = new Random().nextInt(10 ** 5)
        segmentsCount.each { 
            n -> segmentsList << new CustomerSegment("CG_Segment_ID-" + randomSuffux + n, "A Segment Name " + randomSuffux + n, "Segment Description " + randomSuffux + n)
        }
        segmentsList
    }
}