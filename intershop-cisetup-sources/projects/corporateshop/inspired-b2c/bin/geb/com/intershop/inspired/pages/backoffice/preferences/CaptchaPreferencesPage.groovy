package geb.com.intershop.inspired.pages.backoffice.preferences

import geb.Page
import geb.module.Checkbox

class CaptchaPreferencesPage extends Page
{
    static at=
    {
      $('table', 'data-testing-id': 'CaptchaPreferences')
    }
    
    static content = {
        applyButton(required:true) { $('input', type: 'button', name: 'updateSettings') }
        resetButton(required:true) { $('input', type: 'button', name: 'reset') }
        backButton(required:true, to: BackOfficePreferencesPage)  { $('input', type: 'button', name: 'back') }
        
        giftCard(required:true) { $('input', 'name': 'CaptchaSettingsForm_intershop.CaptchaPreferences.RedemptionOfGiftCardsAndCertificates').module(Checkbox) }
        registration(required:true) { $('input', 'name': 'CaptchaSettingsForm_intershop.CaptchaPreferences.Register').module(Checkbox) }
        emailShoppingCard(required:true) { $('input', 'name': 'CaptchaSettingsForm_intershop.CaptchaPreferences.EmailShoppingCart').module(Checkbox) }
        forgotPassword(required:true) { $('input', 'name': 'CaptchaSettingsForm_intershop.CaptchaPreferences.ForgotPassword').module(Checkbox) }
        contactUs(required:true) { $('input', 'name': 'CaptchaSettingsForm_intershop.CaptchaPreferences.ContactUs').module(Checkbox) }
    }
    
    def enableCaptchasForGiftCards() {
        giftCard.check()
    }
    
    def disableCaptchasForGiftCards() {
        giftCard.uncheck()
    }
    
    def enableCaptchasForRegistration() {
        registration.check()
    }
    
    def disableCaptchasForRegistration() {
        registration.uncheck()
    }
    
    def enableCaptchasForShoppingCardEmails() {
        emailShoppingCard.check()
    }
    
    def disableCaptchasForShoppingCardEmails() {
        emailShoppingCard.uncheck()
    }
    
    def enableCaptchasForForgotPassword() {
        forgotPassword.check()
    }
    
    def disableCaptchasForForgotPassword() {
        forgotPassword.uncheck()
    }
    
    def enableCaptchasForContactUs() {
        contactUs.check()
    }
    
    def disableCaptchasForContactUs() {
        contactUs.uncheck()
    }
}