package geb.com.intershop.inspired.pages.storefront.responsive.account

import geb.com.intershop.inspired.pages.storefront.responsive.StorefrontPage;

import geb.Page

class GiftCardBalanceLookupPage extends StorefrontPage
{
    static at=
    {
        waitFor {contentSlot.size()>0}
    }
    
    static content=
    {
        contentSlot  {$("div", class:"row account-main")}
        giftCardField {contentSlot.find("div",class:"col-md-9")}
        cardNumberInput {giftCardField.find("input",id:"GiftCertificateBalance_GiftCardNumber")}
        cardPINInput {giftCardField.find("input",id:"GiftCertificateBalance_GiftPinNumber")}
        checkButton {giftCardField.find("button")}
    }
}
