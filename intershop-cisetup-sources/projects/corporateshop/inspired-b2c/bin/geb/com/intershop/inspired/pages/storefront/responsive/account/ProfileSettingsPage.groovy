package geb.com.intershop.inspired.pages.storefront.responsive.account

import geb.com.intershop.inspired.pages.storefront.responsive.StorefrontPage;

class ProfileSettingsPage extends StorefrontPage
{
    static at=
    {
        waitFor{contentSlot.size()>0}
    }

    static content=
    {
        contentSlot { $("div[data-testing-id='profile-settings-page']") }
        emailField { $("[data-testing-id='email-field']") }
        editButtons { $("a",title:"Edit") }
        profileSection { $("div",class:"row section")[2] }
    }



    //------------------------------------------------------------
    // link functions
    //------------------------------------------------------------

    def clickEditEmail()
    {
        editButtons[0].click()
    }

    def clickEditPassword()
    {
        editButtons[1].click()
    }

    def clickEditProfile()
    {
        editButtons[2].click()
    }


}
