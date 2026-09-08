package geb.com.intershop.inspired.pages.storefront.responsive.account

import geb.com.intershop.inspired.pages.storefront.responsive.StorefrontPage;

class AccountCreateFullPage extends StorefrontPage
{
    static at =
    {
        waitFor{ contentSlot.size()>0}
    }

    static content=
    {
        contentSlot {$("div[data-testing-id='account-create-full-page']")}
        eMailInput {$("input",id:"RegisterUserFullEmail_Login")}
        eMailConfirmInput {$("input" ,id:"RegisterUserFullEmail_EmailConfirmation")}
        passwordInput {$("input",name:"RegisterUserFullEmail_Password")}
        passwordConfirmInput {$("input",name:"RegisterUserFullEmail_PasswordConfirmation")}
        secureQuestionSelector {$("select",name:"RegisterUserFullEmail_SecurityQuestion")}
        secureAnswerInput {$("input",name:"RegisterUserFullEmail_Answer")}

        countrySelector {$("select",name:"AddressForm_CountryCode")}
        fNameInput {$("input", name:"AddressForm_FirstName")}
        lNameInput {$("input",name:"AddressForm_LastName")}
        address1Input {$("input", name:"AddressForm_Address1" )}
        address2Input {$("input", name:"AddressForm_Address2" )}
        postalInput {$("input",name:"AddressForm_PostalCode")}
        cityInput {$("input",name:"AddressForm_City")}
        langSelector {$("select",name:"AddressForm_LocaleID")}

        continueButton {contentSlot.$("button",name:"CreateAccount")}
    }

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

    def setAddress(country,fName,lName,address1,address2,zip,city)
    {
        countrySelector.value country
        sleepForNSeconds(20)
        fNameInput.value fName
        lNameInput.value lName
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

