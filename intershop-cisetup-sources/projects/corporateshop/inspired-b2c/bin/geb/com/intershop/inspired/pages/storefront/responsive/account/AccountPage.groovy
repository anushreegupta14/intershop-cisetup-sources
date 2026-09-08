package geb.com.intershop.inspired.pages.storefront.responsive.account

import geb.com.intershop.inspired.pages.storefront.responsive.StorefrontPage;

import geb.Page

class AccountPage extends StorefrontPage
{
    static at =
    {
        waitFor{contentSlot.size()>0}
    }

    static content=
    {
        contentSlot {$("div[data-testing-id='account-page']")}
        profileSettingsLink {$("a",href:contains("ViewProfileSettings-ViewProfile"),1)}
        changeAddressLink {$("a",href:contains("ViewUserAddressList-List"),1)}
        giftCardBalance {$("a",href:contains("ViewGiftCertificatesBalance-Show"),1)}
        logoutLink {$("a",class:"my-account-link my-account-logout")}
    }


    //------------------------------------------------------------
    // link functions
    //------------------------------------------------------------
    def clickProfileSettings()
    {
        profileSettingsLink.click()

    }

    def clickChangeAddress()
    {
        changeAddressLink.click()
    }


}

