package geb.com.intershop.inspired.pages.storefront.responsive.account

import geb.com.intershop.inspired.pages.storefront.responsive.StorefrontPage;

class ProfileSettingsPasswordPage extends StorefrontPage
{
    static at=
    {
        waitFor{contentSlot.size()>0}
    }

    static content=
    {
        contentSlot {$("div[data-testing-id='profile-settings-password-page']")}
        
        passwordInput {$("input",id:"UpdatePasswordForm_Password")}
        newPasswordInput {$("input",id:"UpdatePasswordForm_NewPassword")}
        newPasswordConfirmInput {$("input",id:"UpdatePasswordForm_NewPasswordConfirmation")}
        secureQuestionSelector {$("select",id:"UpdatePasswordForm_SecurityQuestion")}
        secureAnswerInput {$("input",id:"UpdatePasswordForm_Answer")}
        updateButton {$("button",name:"UpdatePassword")}
    }

    //------------------------------------------------------------
    // link functions
    //------------------------------------------------------------
    def changePassword(oldPw,password,answer)
    {
        passwordInput.value oldPw
        newPasswordInput.value password
        newPasswordConfirmInput.value password
        secureQuestionSelector.value "account.security_question.maiden_name.text"
        secureAnswerInput.value answer
        updateButton.click()
    }
}
