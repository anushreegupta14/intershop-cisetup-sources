package geb.com.intershop.inspired.pages.storefront.responsive.smb

import geb.com.intershop.inspired.pages.storefront.responsive.StorefrontPage

class AccountRegisterPageSMB extends StorefrontPage
{

    static content=
    {
        eMailInput {$("input",id:"RegisterUserFullEmail_Login")}
        eMailConfirmInput {$("input" ,id:"RegisterUserFullEmail_EmailConfirmation")}
        passwordInput {$("input",id:"RegisterUserFullEmail_Password")}
        passwordConfirmInput {$("input",id:"RegisterUserFullEmail_PasswordConfirmation")}
        secureQuestionSelector {$("select",id:"RegisterUserFullEmail_SecurityQuestion")}
        secureAnswerInput {$("input",id:"RegisterUserFullEmail_Answer")}

        cName1Input {$("input", id:"CompanyInformationForm_CompanyName")}
        cName2Input {$("input", id:"CompanyInformationForm_CompanyName2")}
        
        cfNameInput {$("input", id:"CompanyInformationForm_FirstName")}
        clNameInput {$("input", id:"CompanyInformationForm_LastName")}
       
        
        
        countrySelector {$("select",id:"AddressForm_CountryCode")}
        
        
        address1Input {$("input", id:"AddressForm_Address1" )}
        address2Input {$("input", id:"AddressForm_Address2" )}
        postalInput {$("input",id:"AddressForm_PostalCode")}
        cityInput {$("input",id:"AddressForm_City")}
        langSelector {$("select",id:"AddressForm_LocaleID")}
    }
    static at =
    {
        waitFor{ $("div[data-testing-id='account-create-full-page']").size()>0}
    }

    //------------------------------------------------------------
    // Page checks
    //------------------------------------------------------------

    //------------------------------------------------------------
    // link functions
    //------------------------------------------------------------
    def setSettings(eMail,password,answer)
    {
        eMailInput.value eMail
        eMailConfirmInput.value eMail
        passwordInput.value password
        passwordConfirmInput.value password
        secureQuestionSelector.value "account.security_question.maiden_name.text"
        secureAnswerInput.value answer
    }

    def setNames(cName1,cName2,cfName,clName)
    {
        cName1Input.value  cName1
        cName2Input.value  cName2
        
        cfNameInput.value cfName
        clNameInput.value clName
        
    }
    def setAddress(country,address1,address2,zip,city)
    {
        
        countrySelector.value country
        sleepForNSeconds(2)
        address1Input.value address1
        address2Input.value address2
        postalInput.value zip
        cityInput.value city
    }
    
    def sleepForNSeconds(int n)
    {
        Thread.sleep(n * 1000)
    }
}

