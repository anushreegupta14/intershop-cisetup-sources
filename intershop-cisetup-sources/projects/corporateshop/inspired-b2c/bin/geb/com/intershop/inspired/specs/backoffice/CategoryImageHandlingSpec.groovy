package geb.com.intershop.inspired.specs.backoffice

import geb.com.intershop.b2c.model.storefront.responsive.User
import geb.com.intershop.inspired.pages.backoffice.BackOfficeLoginPage
import geb.com.intershop.inspired.pages.backoffice.BackOfficePage
import geb.com.intershop.inspired.pages.backoffice.catalog.CatalogCategoryOverviewOrganizationPage
import geb.com.intershop.inspired.pages.backoffice.catalog.CatalogCategoryOverviewSalesChannelPage
import geb.com.intershop.inspired.pages.backoffice.channels.BackOfficeChannelOverviewPage
import geb.com.intershop.inspired.pages.backoffice.channels.ChannelCatalogListPage
import geb.com.intershop.inspired.pages.backoffice.channels.organization.OrgCatalogListPage
import geb.com.intershop.inspired.pages.backoffice.imagemanagement.ImageManagementPage
import geb.com.intershop.inspired.pages.backoffice.imagemanagement.ImageManagementTypeListPage
import geb.com.intershop.inspired.testdata.TestDataUsage
import geb.spock.GebReportingSpec
import spock.lang.Stepwise
import spock.lang.Ignore

/**
 * Backoffice tests for Catalog Category Image handling at inSPIRED.
 *
 *  In the organization:
 *  - Assign an image to a master catalog category (Backoffice).
 *  - Check, if this image is shown in the Storefront.
 *  In the b2c channel:
 *  - Assign another image to the shared category used before. This should fail (not allowed for shared catalogs).
 *  - Assign the image to a local catalog.
 *  - Check, if this image is shown in the Storefront.
 *
 */

@Stepwise
@Ignore("Assertion failed: Expected M/1454984-9326.jpg, actual M/M_201807204-01_side.jpg")
class CategoryImageHandlingSpec extends GebReportingSpec implements TestDataUsage
{

    // keep the original images to handle invalidation during clean-up
    private static images = new Stack()
  
/*      Do we need to restore the old images?
        
        User b2cUser = new User("admin", "admin", "admin")
        
        given: "Logged in as admin.."
            logInUser(b2cUser, "inSPIRED")

        when: "I want to switch to the B2B Channel"
            selectChannel("inTRONICS Business")
        then:
            at BackOfficeChannelOverviewPage
        
        // goto Catalog -> Catalogs
        
        while ( !images.empty )
        {
            def currImage = images.pop()
            
            ....
        }
        logoutUser()
*/         

    /**
     * Assign a category image to a catalog category in the Master Catalog of inSPIRED.
     * Check if the new image is assigned.
     * 
     */
    def "Assign a category image to a Master catalog category in the BackOffice"() {

        given: "Logged in as admin.."
            User b2cUser = new User("admin", "admin", "admin")
            to BackOfficeLoginPage
        
        when:
            logInUser(b2cUser, "inSPIRED")
            
        then:
            at BackOfficePage

        when: "goto Master Catalogs -> Image Management"
            navigateToMainMenuItem "bo-sitenavbar-catalogs-organization", "link-mastercatalogs-imagemanagement-organization"

        then:"I'm at the Image management page."
            at ImageManagementPage

        when: "goto Master Catalogs -> Image Management -> Image Types"
            clickImageTypesTab()

        then:"I'm at the Image management page, Image Types tab."
            at ImageManagementTypeListPage

        when: "Select the first available image type."
            selectImageTypeByIndex(1)

        then:"There is image type selected."
            hasSelectedImageType()

        when: "goto Master Catalogs -> Catalogs"
            navigateToMainMenuItem "bo-sitenavbar-catalogs-organization", "link-catalogs-catalogs-organization"
 
        then:"I'm at the Catalogs list page."
             at OrgCatalogListPage
 
        when: "click kombjuders"
             openCatalog(masterCatalog)
        
        then: "hier"    
             at CatalogCategoryOverviewOrganizationPage
         
        when: "I move to category.."    
             openCatalog(masterCatalogCategoryLevel1)
             openCatalog(masterCatalogCategoryLevel2)
        and: "..edit category properties,.."
             editCatalogOrViewProperties()
             
        and: "..go to image assignment,.."
             // CatalogCategoryDetailsGeneralOrganizationPage
             assert isPresentImageAssignmentButton()
             clickSelectImageAssignment()
             
        and: "..and assign an image."
             // CatalogCategoryDetailsSelectImageOrganizationPage
             assignImage("M", masterCatalogImage)
             
        then: "The category image is assigned."
             assert checkCategoryImageIsAssigned("M", masterCatalogImage)
                          
        where:
            masterCatalog                   << testData.get("categoryImageHandling.master.catalog.computers.id")
            masterCatalogCategoryLevel1     << testData.get("categoryImageHandling.master.category.notebooks.id")
            masterCatalogCategoryLevel2     << testData.get("categoryImageHandling.master.category.notebooks.accesories.id")
            masterCatalogImage              << testData.get("categoryImageHandling.master.category.image.1")

    }

    /**
     * Assign a category image to a channel catalog category shared from the Master Catalog.
     * This should not be possible, since properties of shared categories are not allowed to be changed.
     * For image handling: the button to select an image for assignment is absent for shared categories.
     * 
     */
    def "Assign a category image to a sales channel's shared catalog category in the BackOffice"() {
        
        given: "Logged in as admin.."
            User b2cUser = new User("admin", "admin", "admin")
            to BackOfficeLoginPage
        
        when:
            logInUser(b2cUser, "inSPIRED")
            
        then:
            at BackOfficePage
        when: "I want to switch to the B2C Channel"
            selectChannel("inTRONICS")
        then:
            at BackOfficeChannelOverviewPage
    
        when: "goto Catalogs -> Catalogs"
            navigateToMainMenuItem "bo-sitenavbar-catalogs-channel", "link-catalogs-catalogs-channel"
 
         then:"I'm at the Catalogs list page."
             at ChannelCatalogListPage
 
             openCatalog(sharedCatalog)

        then: "hier"    
             at CatalogCategoryOverviewSalesChannelPage
             
             openCatalog(sharedCatalogCategoryLevel1)
             openCatalog(sharedCatalogCategoryLevel2)
             
             editCatalogOrViewProperties()
             assert !isPresentImageAssignmentButton()
             assert checkCategoryImageIsAssigned("M", sharedCatalogImage)
             
        where:
            sharedCatalog                   << testData.get("categoryImageHandling.master.catalog.computers.id")
            sharedCatalogCategoryLevel1     << testData.get("categoryImageHandling.master.category.notebooks.id")
            sharedCatalogCategoryLevel2     << testData.get("categoryImageHandling.master.category.notebooks.accesories.id")
            sharedCatalogImage              << testData.get("categoryImageHandling.master.category.image.1")
      
    }

    /**
     * Assign a category image to a local channel catalog category.
     * Check if the new image is assigned.
     * 
     */
    def "Assign a category image to a sales channel's local catalog category in the BackOffice"() {
        
        given: "Logged in as admin.."
            User b2cUser = new User("admin", "admin", "admin")
            to BackOfficeLoginPage
        
        when:
            logInUser(b2cUser, "inSPIRED")
            
        then:
            at BackOfficePage
        when: "I want to switch to the B2C Channel"
            selectChannel("inTRONICS")
        then:
            at BackOfficeChannelOverviewPage
    
        when: "goto Catalogs -> Catalogs"
            navigateToMainMenuItem "bo-sitenavbar-catalogs-channel", "link-catalogs-catalogs-channel"
 
         then:"I'm at the Catalogs list page."
             at ChannelCatalogListPage
 
             openCatalog(localCatalog)

        then: "hier"    
             at CatalogCategoryOverviewSalesChannelPage
             
        when: "I move to category.."    
             openCatalog(localCatalogCategoryLevel1)
             
        and: "..edit category properties,.."
             editCatalogOrViewProperties()
             // CatalogCategoryDetailsGeneralChannelPage

        and: "..go to image assignment,.."
             assert isPresentImageAssignmentButton()
             clickSelectImageAssignment()
             // CatalogCategoryDetailsSelectImageChannelPage
             
        and: "..and assign an image."
             assignImage("M", localCatalogCategoryImage)
             // CatalogCategoryDetailsGeneralChannelPage
             
        then: "The category image is assigned."
             checkCategoryImageIsAssigned("M", localCatalogCategoryImage)

       where:
             localCatalog                   << testData.get("categoryImageHandling.channel.catalog.cameras.id")
             localCatalogCategoryLevel1     << testData.get("categoryImageHandling.channel.category.webcams.id")
             localCatalogCategoryImage      << testData.get("categoryImageHandling.channel.category.image.1")
             
    }

}