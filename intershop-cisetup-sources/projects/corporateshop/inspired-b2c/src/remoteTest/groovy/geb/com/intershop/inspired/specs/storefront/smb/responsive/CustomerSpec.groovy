package geb.com.intershop.inspired.specs.storefront.smb.responsive

import geb.com.intershop.inspired.pages.backoffice.*
import geb.com.intershop.inspired.pages.storefront.responsive.*
import geb.com.intershop.inspired.pages.storefront.responsive.account.*
import geb.com.intershop.inspired.pages.storefront.responsive.checkout.*
import geb.com.intershop.inspired.pages.storefront.responsive.shopping.*
import geb.com.intershop.inspired.pages.storefront.responsive.smb.*
import geb.com.intershop.inspired.testdata.TestDataUsage
import geb.spock.GebReportingSpec
import spock.lang.*

@Ignore
class CustomerSpec extends GebReportingSpec implements TestDataUsage
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
        uncheckSMBCAPTCHAs()
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
        navigateToSMBCustomers()

        and: "...and delete every new one."
        while ( !registeredUsers.empty )
        {
            def name = registeredUsers.pop()
            deleteCustomer name
        }
    }
    /**
     * Register Business User<p>
     *
     * Old Smoke Test:
     * testCreateNewBusinessUserInSF(...)
     */
    def "Register Business User"()
    {
        when: "I go to the Homepage and press SignIn..."
        to HomePageSMB
        at HomePageSMB
        registerLink.click()

        then: "...to go to the RegisterPage."
        at AccountRegisterPageSMB

        when: "I fill all my datas and submit."
        setSettings eMail,password,answer
        setNames cName1,cName2,fName,lName
        setAddress country,address1,address2,zip,city
        $("button",name:"CreateAccount").click()
        registeredUsers.push cName2==""?cName1:(cName1+" "+cName2)

        then: "Now I'm at the HomePage signed in."
        at HomePageSMB
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
        cName1  = testData.get("checkoutUser.smb.company1")[0]
        cName2  = testData.get("checkoutUser.smb.company2")[0]

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
        to HomePageSMB
        at HomePageSMB
        pressLogIn()

        then:
        at AccountLoginPageSMB

        when:
        login user,password

        then: "... then I'm logged in."
        at AccountPage

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
        emailField.text()==newUser

        cleanup: "Now I will rollback my changes."
        clickEditEmail()
        at ProfileSettingsEmailPage
        changeLogin user,password
        at ProfileSettingsPage
        emailField.text()==user

        where:
        
        user        =  testData.get("checkoutUser.smb.login.eMail")[0]
        password    =  testData.get("checkoutUser.smb.login.password")[0]
        newUser     =  testData.get("checkoutUser.smb.login.eMail2")[0]
      
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
        to HomePageSMB
        at HomePageSMB
        pressLogIn()

        then:
        at AccountLoginPageSMB

        when:
        login user,password

        then: "... then I'm logged in."
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
        user        =  testData.get("checkoutUser.smb.login.eMail")[0]
        password    =  testData.get("checkoutUser.smb.login.password")[0]
        newPassword << testData.get("EditYourPassword.password2")
        answer      << testData.get("defaultUser.answer")


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
        to HomePageSMB
        at HomePageSMB
        pressLogIn()

        then:
        at AccountLoginPageSMB

        when:
        login user,password

        then: "... then I'm logged in."
        at AccountPage

        when: "... click at Profile Settings..."
        clickProfileSettings()

        then: "... then i am at profile edit page."
        at ProfileSettingsPage

        when: "Now I click at 'Edit' in profile section..."
        editButtons[3].click()

        then: "... and enter the Profile Setting Page... "
        at ProfileSettingsProfilePage

        when: "... where I can change my profile."
        def backup = [fNameInput.value() ,  lNameInput.value() , phoneInput.value()]
        changePreferences fName,lName,phone

        then: "After all, I see the change in 'My Account'."
        at ProfileSettingsPage
        $("div",class:"row section")[3].text().contains(fName + " " + lName)
        $("div",class:"row section")[3].text().contains(phone)

        when: "Redo"
        editButtons[3].click()

        then: "... and enter the Profile Setting Page... "
        at ProfileSettingsProfilePage
        
        when: "Redo"
        changePreferences backup[0],backup[1],backup[2]
        
        then: ""
        at ProfileSettingsPage
        $("div",class:"row section")[3].text().contains(backup[0] + " " + backup[1])
        $("div",class:"row section")[3].text().contains(backup[2])
        
        where:

        user       = testData.get("checkoutUser.smb.login.eMail")[0]
        password   = testData.get("checkoutUser.smb.login.password")[0]
        fName      << testData.get("EditYourProfile.fName")
        lName      << testData.get("EditYourProfile.lName")
        phone      << testData.get("EditYourProfile.phone")
        
       
    }
    
    /**
     * Edit Your Profile Settings changeProfile<p>
     *
     * Old Smoke Test:
     * testEditProfileAndSettings(...)
     */
    def "Edit Your Profile Settings by changing Company Profile"()
    {
        when: "I go to the signIn page and log in..."
        to HomePageSMB
        at HomePageSMB
        pressLogIn()

        then:
        at AccountLoginPageSMB

        when:
        login user,password

        then: "... then I'm logged in."
        at AccountPage

        when: "... click at Profile Settings..."
        clickProfileSettings()

        then: "... then i am at profile edit page."
        at ProfileSettingsPage

        when: "Now I click at 'Edit' in profile section..."
        editButtons[2].click()

        then: "... and enter the Profile Setting Page... "
        at CompanyProfileEditPage
        
        when: "... where I can change my profile."
        def backup = cNameInput.value()
        cNameInput.value companyName
        updateButton.click()
        
        then: "After all, I see the change in 'My Account'."
        at ProfileSettingsPage
        $("div",class:"row section")[2].text().contains(companyName)
        
        when: "Redo"
        editButtons[2].click()

        then: "... and enter the Profile Setting Page... "
        at CompanyProfileEditPage
        
        when: "... where I can change my profile."
        cNameInput.value backup
        updateButton.click()
        
        then:
        at ProfileSettingsPage
        $("div",class:"row section")[2].text().contains(backup)
        
        where:
        user       = testData.get("checkoutUser.smb.login.eMail")[0]
        password   = testData.get("checkoutUser.smb.login.password")[0]
      

        companyName      =testData.get("checkoutUser.smb.company1")[0]
      
       
    }
    
    /**
     * Add a New Address in Business User<p>
     *
     * Old Smoke Test:
     * AddCustomerAddressFromStorefront(...)
     */
    def "Add a New Address in Business Customer"()
    {
        when: "I go to the signIn page and log in..."
        to HomePageSMB
        at HomePageSMB
        pressLogIn()

        then:
        at AccountLoginPageSMB

        when:
        login user,password

        then: "... then I'm logged in."
        at AccountPage

        when: "... click at Saved Addresses... "
        clickChangeAddress()

        then: "... see my Addresses."
        at AccountAddressesPage
        
        when: "I click add Addresses..."
        addAddressButton.click()
        
        and: "...and fill in my datas."
        setAddress country,fName,lName,address1,address2,zip,city
        $("input",id:"address_CompanyName").value companyName
        saveAddressButton.click()
        
        then: "After Refresh I can find my new address..."
        at AccountAddressesPage
        
        when:
        sleepForNSeconds(4)
        driver.navigate().refresh();
        sleepForNSeconds(4)
        
        then:
        waitFor {$("address",text:iContains(address1)).size()>0}
        def block = $("address",text:iContains(address1)).parent().parent().parent();
        
        and: "... filled correctly."
        block.text().contains(fName+" "+lName)
        block.text().contains(address2)
        block.text().contains(zip)
        block.text().contains(city)
       
      
        when:
        block.find("a",title:iContains("delete")).click()
        
        then:
        at AddressConfirmModal
        confirmButton.click()
        
        where:
        user     = testData.get("checkoutUser.smb.login.eMail")[0]
        password = testData.get("checkoutUser.smb.login.password")[0]
        
        country << testData.get("UpdateDefaultCustomer.country")
        fName   << testData.get("UpdateDefaultCustomer.fName")
        lName   << testData.get("UpdateDefaultCustomer.lName")
        address1<< testData.get("UpdateDefaultCustomer.address1")
        address2<< testData.get("UpdateDefaultCustomer.address2")
        zip     << testData.get("UpdateDefaultCustomer.zip")
        city    << testData.get("UpdateDefaultCustomer.city")
        
        companyName << testData.get("checkoutUser.smb.company1")
        
    }
    
}
