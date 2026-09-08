package geb.com.intershop.inspired.pages.storefront.responsive.account

import geb.com.intershop.inspired.pages.storefront.responsive.StorefrontPage;

class ProfileSettingsEmailPage extends StorefrontPage
{
    static at=
    {
        waitFor {contentSlot.size()>0}
    }

    static content=
    {
        contentSlot {$("div[data-testing-id='profile-settings-email-page']")}
        emailInput {$("input",id:"UpdateEmailForm_Email")}
        eMailConfirmInput {$('input', id:'UpdateEmailForm_EmailConfirmation')}
        passwordInput {$('input', id:'UpdateEmailForm_Password')}
        updateButton {$('button', name:'UpdateEmail')}
    }


    //------------------------------------------------------------
    // link functions
    //------------------------------------------------------------
    def changeLogin(newMail,password)
    {
        emailInput.value        newMail
        eMailConfirmInput.value newMail
        passwordInput.value     password

        updateButton.click()
    }
}
