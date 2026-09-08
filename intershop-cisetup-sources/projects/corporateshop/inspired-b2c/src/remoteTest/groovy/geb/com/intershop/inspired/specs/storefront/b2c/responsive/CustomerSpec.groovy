package geb.com.intershop.inspired.specs.storefront.b2c.responsive

import geb.com.intershop.inspired.pages.backoffice.*
import geb.com.intershop.inspired.pages.storefront.responsive.*
import geb.com.intershop.inspired.pages.storefront.responsive.account.*
import geb.com.intershop.inspired.pages.storefront.responsive.checkout.*
import geb.com.intershop.inspired.pages.storefront.responsive.shopping.*
import geb.com.intershop.inspired.testdata.TestDataUsage
import geb.spock.GebReportingSpec
import spock.lang.*

/**
 * Storefront tests for Customer Management at inSPIRED, 
 * based on old Smoke Tests.
 * @author Sebastian Glass
 *
 */
@Ignore
class CustomerSpec extends GebReportingSpec  implements TestDataUsage
{
    
    // Stack for Test: "Register Individual Customer"
    private static registeredUsers = new Stack()
    
     /**
     * Runs before a test in this Spec will run once. 
     * Deactivates the CAPTCHAs for Registration in backoffice. 
     */
    def setupSpec()
    {
        when: "I go to the backoffice..."
        to BOLoginPage
        at BOLoginPage

        then: "...and unable CAPTCHAs for Registration."
        uncheckB2CCAPTCHAs()
    }

    /**
     * Runs after all tests in this Spec run once.
     * Deletes all new registered users to enable multiple runs.
     */
    def cleanupSpec()
    {
        when: "I go to the backoffice..."
        to BOLoginPage
        at BOLoginPage

        then: "...go to the customer list..."
        navigateToB2CCustomers()

        and: "...and delete every new one."
        while ( !registeredUsers.empty )
        {
            def name = registeredUsers.pop()
            deleteCustomer name
        }
    }
    /**
     * Register Individual Customer<p>
     * 
     * Old Smoke Test: 
     * testCreateNewIndividualCustomerInSF(...)
     */
    def "Register Individual Customer"()
    {
        when: "I go to the Homepage and press SignIn..."
        to HomePage
        at HomePage
        registerLink.click()

        then: "...to go to the RegisterPage."
        at AccountCreateFullPage

        when: "I fill all my datas and submit."
        setSettings eMail,password,answer
        setAddress country,fName,lName,address1,address2,zip,city
        continueButton.click()
        registeredUsers.push fName + " " + lName

        then: "Now I'm at the HomePage signed in."
        at HomePage
        isSignedIn(true)

        where:

        eMail   = testData.get("RegisterIndividualCustomer.eMail")[0]
        password= testData.get("RegisterIndividualCustomer.password")[0]
        answer  = testData.get("defaultUser.answer")[0]
        country = testData.get("defaultUser.address.country")[0]
        fName   = testData.get("RegisterIndividualCustomer.fName")[0]
        lName   = testData.get("RegisterIndividualCustomer.lName")[0]
        address1= testData.get("defaultUser.address.address1")[0]
        address2= testData.get("defaultUser.address.address2")[0]
        zip     = testData.get("defaultUser.address.zip")[0]
        city    = testData.get("defaultUser.address.city")[0]

    }

    /**
     * Add a New Address in Individual Customer<p>
     *
     * Old Smoke Test:
     * testAddCustomerAddressFromStorefront(...)
     */
    def "Add a New Address in Individual Customer"()
    {
        when: "I go to the signIn page and log in..."
        to HomePage
        at HomePage
        pressLogIn()

        at AccountLoginPage
        login user,password

        then: "... then I'm at the MyAccountPage... "
        at AccountPage

        when: "... click at Saved Addresses... "
        clickChangeAddress()

        then: "... see my Addresses."
        at AccountAddressesPage

        when: "I click add Addresses..."
        addAddressButton.click()
        sleepForNSeconds(20)
        
        and: "...and fill in my datas."
        setAddress country,fName,lName,address1,address2,zip,city
        saveAddressButton.click()
        sleepForNSeconds(5)

        and:
        driver.navigate().refresh();

        then:
        waitFor {$("address",text:iContains(address1)).size()>0}
        def block = $("address",text:iContains(address1)).closest("div",class:startsWith("myAccount-addressBox")).parent();

        and: "... filled correctly."
        block.text().contains(fName+" "+lName)
        block.text().contains(address2)
        block.text().contains(zip)
        block.text().contains(city)


        and:" So I can delete it."
        def buttonfield = block.find("div",id:startsWith("myAccount-actionLinks-"))

        when:
        buttonfield.find("a",class:"remove-address").click()

        then:
        at AddressConfirmModal
        confirmButton.click()

        where:
      
        user     = testData.get("defaultUser.login.eMail")[0]
        password = testData.get("defaultUser.login.password")[0]
        
        country << testData.get("UpdateDefaultCustomer.country")
        fName   << testData.get("UpdateDefaultCustomer.fName")
        lName   << testData.get("UpdateDefaultCustomer.lName")
        address1<< testData.get("UpdateDefaultCustomer.address1")
        address2<< testData.get("UpdateDefaultCustomer.address2")
        zip     << testData.get("UpdateDefaultCustomer.zip")
        city    << testData.get("UpdateDefaultCustomer.city")
    }


    /**
     * Update Default Customer Address from Storefront<p>
     *
     * Old Smoke Test:
     * testUpdateDefaultCustomerAddressFromStorefront(...)
     */
    def "Update Default Customer Address from Storefront"()
    {
        when: "I go to the signIn page and log in..."
        to HomePage
        at HomePage
        pressLogIn()

        at AccountLoginPage
        login user,password

        then: "... then I'm at the MyAccountPage... "
        at AccountPage

        when: "... click at Saved Addresses... "
        clickChangeAddress()

        then: "... see my Addresses."
        at AccountAddressesPage
        waitFor {$("address",text:iContains(address1)).size()>0}
        def block = $("address",text:iContains(address1)).parent().parent();

        and: "... filled correctly."
        block.text().contains(fName+" "+lName)
        block.text().contains(address2)
        block.text().contains(zip)
        block.text().contains(city)

        and:" So I can delete it."
        def buttonfield = block.find("div",class:"col-xs-4 text-right")

        when:
        buttonfield.find("a",title:"Edit this address").click()

        //
        and: "...and fill in my datas."
       
        updateAddress country,fName2,lName2,address12,address22,zip2,city2
        saveAddressButton[1].click()
        saveAddressButton[1].click()

        then:""
        waitFor{$("div",class:"myAccount-addressBook")}

        where:
        user        = testData.get("defaultUser.login.eMail")[0]
        password    = testData.get("defaultUser.login.password")[0]
        country     = testData.get("defaultUser.address.country")[0]
        fName       = testData.get("defaultUser.address.fName")[0]
        lName       = testData.get("defaultUser.address.lName")[0]
        address1    = testData.get("defaultUser.address.address1")[0]
        address2    = testData.get("defaultUser.address.address2")[0]
        zip         = testData.get("defaultUser.address.zip")[0]
        city        = testData.get("defaultUser.address.city")[0]
        
        fName2     << testData.get("UpdateDefaultCustomer.fName")
        lName2     << testData.get("UpdateDefaultCustomer.lName")
        address12  << testData.get("UpdateDefaultCustomer.address1")
        address22  << testData.get("UpdateDefaultCustomer.address2")
        zip2       << testData.get("UpdateDefaultCustomer.zip")
        city2      << testData.get("UpdateDefaultCustomer.city")
        
    }


    /**
     * Edit Your Profile Settings changeEmail<p>
     *
     * Old Smoke Test:
     * testEditProfileAndSettings(...)
     */
    def "Edit Your Profile Settings by changing Email"()
    {
        when: "I go to the signIn page and log in..."
        to HomePage
        at HomePage
        pressLogIn()

        then:
        at AccountLoginPage
        
        when:
        login user,password


        then: "... then I'm at the MyAccountPage... "
        at AccountPage

        when: "... click at Profile Settings..."
        clickProfileSettings()

        then: "... then I am at profile edit page."
        at ProfileSettingsPage

        when: "Now I click at 'Edit' in Email section..."
        clickEditEmail()

        then: "... and enter the Login Setting Page... "
        at ProfileSettingsEmailPage


        when: "... where I can change my login."
        changeLogin newUser,password

        then: "After all, I see the change in 'My Account'."
        at ProfileSettingsPage
        emailField.text() == newUser

        cleanup: "Now I will rollback my changes."
        at ProfileSettingsPage
        clickEditEmail()
        at ProfileSettingsEmailPage
        changeLogin user,password
        at ProfileSettingsPage
        emailField.text() == user

        where:
        user       =  testData.get("defaultUser.login.eMail")[0]                  
        password   =  testData.get("defaultUser.login.password")[0]    
        newUser    << testData.get("EditYourEmail.eMail2")

    }

    /**
     * Edit Your Profile Settings changePassword<p>
     *
     * Old Smoke Test:
     * testEditProfileAndSettings(...)
     */
    def "Edit Your Profile Settings by changing Password"()
    {
        when: "I go to the signIn page and log in..."
            to HomePage
        then:
            at HomePage
        when:
            pressLogIn()

        then:
            at AccountLoginPage
        when:
            login user,password

        then:"... then I'm at the MyAccountPage... "
            at AccountPage

        when: "... click at Profile Settings..."
            clickProfileSettings()

        then: "... then i am at profile edit page."
            at ProfileSettingsPage

        when: "Now I click at 'Edit' in password section..."
            clickEditPassword()

        then: "... and enter the Password Setting Page... "
        at ProfileSettingsPasswordPage

        when: "... where I can change my password."
        changePassword password,newPassword,answer

        then: "After all, I see the change in 'My Account'."
        at ProfileSettingsPage

        cleanup: "Now I will rollback my changes."
        clickEditPassword()
        at ProfileSettingsPasswordPage
        changePassword newPassword,password,answer
        at ProfileSettingsPage

        where:
        user       = testData.get("defaultUser.login.eMail")[0]
        password   = testData.get("defaultUser.login.password")[0]
        newPassword<< testData.get("EditYourPassword.password2")
        answer     << testData.get("defaultUser.answer")
       

    }

    /**
     * Edit Your Profile Settings changeProfile<p>
     *
     * Old Smoke Test:
     * testEditProfileAndSettings(...)
     */
    def "Edit Your Profile Settings by changing Profile"()
    {
        when: "I go to the signIn page and log in..."
        to HomePage
        at HomePage
        pressLogIn()

        at AccountLoginPage
        login user,password

        then:"... then I'm at the MyAccountPage... "
        at AccountPage

        when: "... click at Profile Settings..."
        clickProfileSettings()

        then: "... then i am at profile edit page."
        at ProfileSettingsPage

        when: "Now I click at 'Edit' in profile section..."
        clickEditProfile()

        then: "... and enter the Profile Setting Page... "
        at ProfileSettingsProfilePage

        when: "... where I can change my profile."
        def backup = [fNameInput.value() ,  lNameInput.value() , phoneInput.value()]
        changePreferences fName,lName,phone

        then: "After all, I see the change in 'My Account'."
        at ProfileSettingsPage
        profileSection.text().contains(fName + " " + lName)
        profileSection.text().contains(phone)

        when: "Now I click at 'Edit' in profile section..."
        clickEditProfile()

        then: "... and enter the Profile Setting Page... "
        at ProfileSettingsProfilePage
        
        when: "Redo"
        changePreferences backup[0],backup[1],backup[2]
        
        then: ""
        at ProfileSettingsPage
        profileSection.text().contains(backup[0] + " " + backup[1])
        profileSection.text().contains(backup[2])
        where:
        
        user       = testData.get("defaultUser.login.eMail")[0]
        password   = testData.get("defaultUser.login.password")[0]
        fName      << testData.get("EditYourProfile.fName")
        lName      << testData.get("EditYourProfile.lName")
        phone      << testData.get("EditYourProfile.phone")
       
    }


    /**
     * Logout as Registered Individual Customer<p>
     *
     * Old Smoke Test:
     * testLogoutAsRegisteredIndividualCustomer(...)
     */
    def "Logout as Registered Individual Customer"()
    {
        when: "I go to the signIn page and log in..."
        to HomePage
        at HomePage
        pressLogIn()

        at AccountLoginPage
        login user,password

        then: "... then I'm at the MyAccountPage... "
        at AccountPage

        when: "... and click lockout."
        logoutLink.click()

        then: "Now I'm not longer logged in."
        at HomePage
        isSignedIn false

        where:
        user      = testData.get("defaultUser.login.eMail")[0]
        password  = testData.get("defaultUser.login.password")[0]
       
    }
}
