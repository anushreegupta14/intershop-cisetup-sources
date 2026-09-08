package geb.com.intershop.inspired.pages.backoffice

import geb.Page
import geb.com.intershop.b2c.model.storefront.responsive.User

class BOLoginPage extends Page
{
    static url = "/INTERSHOP/web/WFS/SLDSystem"
    static at=
    {
        waitFor{loginDiv.size()>0}
    }

    static content =
    {
		loginDiv		  { $("div", "data-testing-id":"page-bo-login")}
        loginInput        { $('input', id: 'LoginForm_Login') }
        passwordInput     { $('input', id: 'LoginForm_Password') }
        organizationInput { $('input', id: 'LoginForm_RegistrationDomain') }
        loginButton(to: [BackOfficePage, BackOfficeLoginPage]) { $('input', "data-testing-id":'btn-login') }
        siteHeader     { $('#main_header') }
    }
    
    def loginUser(user,password, organization)
    {
        loginInput.value   user
        passwordInput.value   password
        organizationInput.value organization
        
        loginButton.click()

    }

    BackOfficePage login(User user, organization)
    {
        loginUser(user.email, user.password, organization)
        browser.page
    }
    
    def sleepForNSeconds(int n)
    {
        Thread.sleep(n * 1000)
    }

    def login(name,password,org)
    {
        $("input",id:"LoginForm_Login").value name
        $("input",id:"LoginForm_Password").value password
        $("input",id:"LoginForm_RegistrationDomain").value org
        loginButton.click()

    }
    
    def logout()
    {
        $("li",class:"logout").find("a").click()
    }
    
    def toPartnerChannel()
    {
        when: "There is a dropdown at the upper right corner..."
        waitFor{$("body",class:"backoffice").size()>0}
        waitFor{$("button",class:"ui-dropdown ui-widget ui-state-default ui-corner-all channel-select-application")}

        then: "...where I choose the B2C sales channel."
        interact
        {
            moveToElement( $("button",class:"ui-dropdown ui-widget ui-state-default ui-corner-all channel-select-application"))
            click()
            moveToElement($("li",class:"application-type-intershop_PartnerBackoffice application-level-channel "))
            click()

        }
    }
    
    def selectChannel(channel) {
        def chSelectForm = siteHeader.$('form', name: 'ChannelSelectForm')
        
        js.exec 'jQuery("#channel-select-application").css("display", "block");'
        
        chSelectForm.ApplicationID = channel
        js.exec "jQuery('form[name=ChannelSelectForm]').submit();"
    }
    
    def toB2CSalesChannel()
    {
        when: "There is a dropdown at the upper right corner..."
        waitFor{$("body",class:"backoffice").size()>0}
        waitFor{$("button",class:"ui-dropdown ui-widget ui-state-default ui-corner-all channel-select-application")}

        then: "...where I choose the B2C sales channel."
        interact
        {
            moveToElement( $("button",class:"ui-dropdown ui-widget ui-state-default ui-corner-all channel-select-application"))
            click()
            waitFor{$("li",class:"application-type-intershop_B2CBackoffice application-level-channel ").size()>0}
            moveToElement($("li",class:"application-type-intershop_B2CBackoffice application-level-channel ",0))
            click()

        }
            
        then: "I wait for the correct side..."
        waitFor{$("table.wrapper").find("a",href:contains("ViewDomainPreference_52-Start")).size()>0}
                  
    }

    def toSMBSalesChannel()
    {
        when: "There is a dropdown at the upper right corner..."
        waitFor{$("body",class:"backoffice").size()>0}
        waitFor{$("button",class:"ui-dropdown ui-widget ui-state-default ui-corner-all channel-select-application")}

        then: "...where I choose the SMB sales channel."
        interact
        {
            moveToElement( $("button",class:"ui-dropdown ui-widget ui-state-default ui-corner-all channel-select-application"))
            click()
            moveToElement($("li",class:"application-type-intershop_B2CBackoffice application-level-channel ",1))
            click()

        }
    }

    def uncheckB2CCAPTCHAs()
    {
        when:
        login "admin","!InterShop00!","inSPIRED"

        and:
        toB2CSalesChannel()
         
        when: "I click the Preference link..."
        $("a[data-testing-id='bo-sitenavbar-preferences']").click()

        
        then: "...and wait for CAPTCHAs."
        waitFor{$("a",text:contains("CAPTCHAs")).size()>0}
        
        when: "I click at the CAPTCHAs-Link..."
        $("a",text:contains("CAPTCHAs")).click()
        
        then: "...wait for the CAPTCHAs input..."
        waitFor{$("input",name:"CaptchaSettingsForm_intershop.CaptchaPreferences.Register").size()>0}
        
        when: "...and uncheck it."
        if($("input",name:"CaptchaSettingsForm_intershop.CaptchaPreferences.Register",checked:"checked").size()>0){
            $("input",name:"CaptchaSettingsForm_intershop.CaptchaPreferences.Register",checked:"checked").click()
            $("input",name:"updateSettings",value:"Apply").click()

        }

        then: "Done... Back to Test."
        waitFor{$("input",name:"CaptchaSettingsForm_intershop.CaptchaPreferences.Register").size()>0}
        
        logout()

    }
    
    def checkB2CCAPTCHAs()
    {
        when:
        login "admin","!InterShop00!","inSPIRED"

        and:
        toB2CSalesChannel()

        then: "I wait for the correct side..."
        waitFor{$("a",href:contains("Preference")).size()>0}

        when: "I click the Preference link..."
        $("a[data-testing-id='bo-sitenavbar-preferences']").click()


        then: "...and wait for CAPTCHAs."
        waitFor{$("a",text:contains("CAPTCHAs")).size()>0}

        when: "I click at the CAPTCHAs-Link..."
        $("a",text:contains("CAPTCHAs")).click()

        then: "...wait for the CAPTCHAs input..."
        waitFor{$("input",name:"CaptchaSettingsForm_intershop.CaptchaPreferences.Register").size()>0}

        when: "...and check it."
        if($("input",name:"CaptchaSettingsForm_intershop.CaptchaPreferences.Register").size() > 0
            && $("input",name:"CaptchaSettingsForm_intershop.CaptchaPreferences.Register",checked:"checked").size() == 0){
            $("input",name:"CaptchaSettingsForm_intershop.CaptchaPreferences.Register").click()
            $("input",name:"updateSettings",value:"Apply").click()
        }

        then: "Done... Back to Test."
        waitFor{$("input",name:"CaptchaSettingsForm_intershop.CaptchaPreferences.Register").size()>0}
        
        logout()

    }

    def uncheckSMBCAPTCHAs()
    {
        when:
        login "admin","!InterShop00!","inSPIRED"

        and:
        toSMBSalesChannel()

        then: "I wait for the correct side..."
        waitFor{$("a",href:contains("Preference")).size()>0}

        when: "I click the Preference link..."

        $("a[data-testing-id='bo-sitenavbar-preferences']").click()


        then: "...and wait for CAPTCHAs."
        waitFor{$("a",text:contains("CAPTCHAs")).size()>0}


        when: "I click at the CAPTCHAs-Link..."
        $("a",text:contains("CAPTCHAs")).click()

        then: "...wait for the CAPTCHAs input..."
        waitFor{$("input",name:"CaptchaSettingsForm_intershop.CaptchaPreferences.Register").size()>0}

        when: "...and uncheck it."
        if($("input",name:"CaptchaSettingsForm_intershop.CaptchaPreferences.Register",checked:"checked").size()>0){
            $("input",name:"CaptchaSettingsForm_intershop.CaptchaPreferences.Register",checked:"checked").click()
            $("input",name:"updateSettings",value:"Apply").click()

        }

        then: "Done... Back to Test."
        waitFor{$("input",name:"CaptchaSettingsForm_intershop.CaptchaPreferences.Register").size()>0}

    }
    
    def checkSMBCAPTCHAs()
    {
        when:
        login "admin","!InterShop00!","inSPIRED"

        and:
        toSMBSalesChannel()

        then: "I wait for the correct side..."
        waitFor{$("a",href:contains("Preference")).size()>0}

        when: "I click the Preference link..."

        $("a[data-testing-id='bo-sitenavbar-preferences']").click()


        then: "...and wait for CAPTCHAs."
        waitFor{$("a",text:contains("CAPTCHAs")).size()>0}


        when: "I click at the CAPTCHAs-Link..."
        $("a",text:contains("CAPTCHAs")).click()

        then: "...wait for the CAPTCHAs input..."
        waitFor{$("input",name:"CaptchaSettingsForm_intershop.CaptchaPreferences.Register").size()>0}

        when: "...and check it."
        if($("input",name:"CaptchaSettingsForm_intershop.CaptchaPreferences.Register").size()>0 &&
             $("input",name:"CaptchaSettingsForm_intershop.CaptchaPreferences.Register",checked:"checked").size() == 0){
            $("input",name:"CaptchaSettingsForm_intershop.CaptchaPreferences.Register").click()
            $("input",name:"updateSettings",value:"Apply").click()

        }

        then: "Done... Back to Test."
        waitFor{$("input",name:"CaptchaSettingsForm_intershop.CaptchaPreferences.Register").size()>0}

    }

    def navigateToB2CCustomers(){
        when:
        login "admin","!InterShop00!","inSPIRED"

        and:
        toB2CSalesChannel()

        when: "I wait for the Customerlink..."
        waitFor{$("a",text:contains("Customers")).size()>0}

        then: "...click it..."
        $("a[data-testing-id='bo-sitenavbar-customers']").click()

        when: "...search for the Subtitle..."
        waitFor{$("a",text:contains("Customers"),class:"overview_subtitle").size()>0}

        then: "...and click it."
        $("a",text:contains("Customers"),class:"overview_subtitle").click()

    }

    def navigateToSMBCustomers(){
        when:
        login "admin","!InterShop00!","inSPIRED"
        
        and:
        toSMBSalesChannel()

        when: "I wait for the Customerlink..."
        waitFor{$("a",text:contains("Customers")).size()>0}

        then: "...click it..."
        $("a[data-testing-id='bo-sitenavbar-customers']").click()

        when: "...search for the Subtitle..."
        waitFor{$("a",text:contains("Customers")).size()>0}

        then: "...and click it."
        $("a",text:contains("Customers"),class:"overview_subtitle").click()

    }

    def deleteCustomer(name){
        when: "search customer with name " + name
        $("input",id:"CustomerSearch_NameOrID").value name
        $("input",value:"Find",class:"button").click()

        then: "result list contains customer name"
        waitFor{$("a",text:iContains(name)).size()>0}

        when: "click on customer "
        $("a",text:iContains(name)).click()

        then: "at customer detail page with delete button"
        sleepForNSeconds(2)
        waitFor{$("input",value:"Delete",class:"button").size()>0}

        when: "click on customer delete"
        $("input",value:"Delete",class:"button").click()

        then: "click confirmation OK to delete "
        waitFor{$("input",name:"delete",value:"OK",class:"button").size()>0}
        $("input",name:"delete",value:"OK",class:"button").click()
    }
    
    
    def navigateToB2CProducts(){
        when:
        login "admin","!InterShop00!","inSPIRED"

        and:
        toB2CSalesChannel()

        when: "I wait for the Catalogs..."
        waitFor{$("a",text:contains("Catalogs")).size()>0}

        then: "...click it..."
        $("a",text:contains("Catalogs")).click()

        when: "...search for the Products..."
        waitFor{$("a",text:contains("Products")).size()>0}

        then: "...and click it."
        $("a",text:contains("Products")).click()
    }
    
	/**
     * Helper method to select a menu item from the top main navigation bar in the backoffice
	 * ASSUMPTION: hyperlinks in menu and menu item has "data-testing-linkid" HTML attribute set
     */
	void navigateToMainMenuItem(String menuId, String menuItemId) {
        when: "There is a navigation menu at the top..."
        waitFor{$("body",class:"backoffice").size()>0}
        waitFor{$("div", id:"main_navigation")}

        then: "...where I open the menu and clik on menu item."
        interact
        {
            moveToElement( $("a", "data-testing-linkid": menuId))
            moveToElement( $("a", "data-testing-linkid": menuItemId))
            click()
        }
	}
	
    def navigateToB2CCartSettings(){
        when:
        login "admin","!InterShop00!","inSPIRED"

        and:
        toB2CSalesChannel()

        when: "I wait for the Customerlink..."
        waitFor{$("a",text:contains("Applications")).size()>0}

        then: "...click it..."
        $("a",text:contains("Applications"),0).click()

        when: "...search for the Application..."
        waitFor{$("a",text:contains("intershop.B2CResponsive"),class:"table_detail_link").size()>0}

        then: "...and click it."
        $("a",text:contains("intershop.B2CResponsive"),class:"table_detail_link").click()
        
        when: "...search for cart and checkout settings..."
        waitFor{$("a",text:contains("Shopping Cart & Checkout"),class:"table_tabs_dis").size()>0}

        then: "...and click it."
        $("a",text:contains("Shopping Cart & Checkout"),class:"table_tabs_dis").click()
    }
}
