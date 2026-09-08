package geb.com.intershop.inspired.pages.storefront.responsive.account

import geb.com.intershop.inspired.pages.storefront.responsive.StorefrontPage;




class AccountAddressesPage extends StorefrontPage
{
    static at=
    {
        waitFor{contentSlot.size()>0}
    }

    static content=
    {
        contentSlot { $("div[data-testing-id='account-addresses-page']") } 
        addAddressButton {$("a",class:"my-account-add-address")}
           
        countrySelector {$("select",id:"address_CountryCode")}
        fNameInput {$("input", id:"address_FirstName")}
        lNameInput {$("input",id:"address_LastName")}
        address1Input {$("input", id:"address_Address1" )}
        address2Input {$("input", id:"address_Address2" )}
        postalInput {$("input",id:"address_PostalCode")}
        cityInput {$("input",id:"address_City")}
        
        saveAddressButton {contentSlot.$("button",type:"submit")}
  
    }
    
    def checkAddress()
    {
        $("address").size() >0
    }
        
    def setAddress(country,fName,lName,address1,address2,zip,city)
    {
        sleepForNSeconds(2)
        countrySelector.value country
        sleepForNSeconds(20)
        fNameInput.value fName
        lNameInput.value lName
        address1Input.value address1
        address2Input.value address2
        postalInput.value zip
        cityInput.value city
    }
    
    def updateAddress(country,fName,lName,address1,address2,zip,city)
    {
        sleepForNSeconds(2)
        countrySelector[1].value country
        sleepForNSeconds(20)
        fNameInput[1].value fName
        lNameInput[1].value lName
        address1Input[1].value address1
        address2Input[1].value address2
        postalInput[1].value zip
        cityInput[1].value city
    }
}
