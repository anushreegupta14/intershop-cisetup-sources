package geb.com.intershop.inspired.pages.storefront.responsive.checkout

import geb.com.intershop.inspired.pages.storefront.responsive.StorefrontPage;

class NewUserAddressPage extends StorefrontPage
{
    static at=
    {
        waitFor { contentSlot.present }
    }

    static content =
    {
        contentSlot     { $("div[data-testing-id='new-user-address-page']") }
        waitFor         { contentSlot.present }
        countrySelector { contentSlot.$('select',name:"billing_CountryCode") }
        fNameInput      { contentSlot.$('input',name:"billing_FirstName") }
        lNameInput      { contentSlot.$('input',name:"billing_LastName") }
        address1Input   { contentSlot.$('input',name:"billing_Address1") }
        address2Input   { contentSlot.$('input',name:"billing_Address2") }
        cityInput       { contentSlot.$('input',name:"billing_City") }
        postalInput     { contentSlot.$('input',name:"billing_PostalCode") }
        eMailInput      { contentSlot.$('input',name:"email_Email") }
        continueButton  { contentSlot.$("button",name:"continue") }
        shipToDifferentAddressButton { contentSlot.$('input', class:"alternate-address") }
        shipToMultipleAddressButton { contentSlot.$('input', id:"shipOption3") }
    }

    //------------------------------------------------------------
    // link functions
    //------------------------------------------------------------
    def fillData(country,fName,lName,address,city,postal,user)
    {
        countrySelector.value(country)
        sleepForNSeconds(20)
        fNameInput.value    fName
        lNameInput.value    lName
        address1Input.value address
        //address2Input.value
        cityInput.value     city
        postalInput.value   postal
        eMailInput.value    user
    }
    
    def fillShippingData(country,fName,lName,address,city,postal)
    {
        contentSlot.$('select',name:"shipping_CountryCode").value(country)
        sleepForNSeconds(20)
        contentSlot.$('input',name:"shipping_FirstName").value fName
        contentSlot.$('input',name:"shipping_LastName").value lName
        contentSlot.$('input',name:"shipping_Address1").value address
        contentSlot.$('input',name:"shipping_City").value city
        contentSlot.$('input',name:"shipping_PostalCode").value postal
        if (!contentSlot.$('button', name:"addNewBilling").empty)
        {
            contentSlot.$('button', name:"addNewBilling").click()
        }
    }
    
    def continueClick()
    {
        continueButton.click()
    }

}
