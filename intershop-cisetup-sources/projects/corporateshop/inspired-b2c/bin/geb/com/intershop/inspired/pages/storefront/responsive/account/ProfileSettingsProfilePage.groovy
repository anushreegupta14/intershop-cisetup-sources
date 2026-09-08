package geb.com.intershop.inspired.pages.storefront.responsive.account

import geb.com.intershop.inspired.pages.storefront.responsive.StorefrontPage;

class ProfileSettingsProfilePage extends StorefrontPage
{

    static at=
    {
        waitFor{ contentSlot.size()>0 }
    }

    static content=
    {
        contentSlot {$("div[data-testing-id='profile-settings-profile-page']")}
        fNameInput {$("input",id:"UpdateProfileForm_FirstName")}
        lNameInput {$("input",id:"UpdateProfileForm_LastName")}
        phoneInput {$("input",id:"UpdateProfileForm_Phone")}
        updateButton { $("button",name:"UpdateProfile")}
    }

    //------------------------------------------------------------
    // link functions
    //------------------------------------------------------------
    def changePreferences(fName,lName,phone)
    {
        fNameInput.value fName
        lNameInput.value lName
        phoneInput.value phone
        updateButton.click()
    }
}
