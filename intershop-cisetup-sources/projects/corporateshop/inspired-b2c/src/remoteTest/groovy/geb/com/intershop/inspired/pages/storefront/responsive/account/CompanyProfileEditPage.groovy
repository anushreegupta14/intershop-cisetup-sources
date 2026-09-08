package geb.com.intershop.inspired.pages.storefront.responsive.account

import geb.com.intershop.inspired.pages.storefront.responsive.StorefrontPage;

class CompanyProfileEditPage extends StorefrontPage
{

    static at=
    {
        waitFor{ $("div",class:"row account-main").size()>0 }
    }

    static content=
    {
        cNameInput {$("input",id:"UpdateCompanyProfileForm_CompanyName_FormValue_CompanyName")}
       
        updateButton { $("button",name:"UpdateProfile")}
    }

    //------------------------------------------------------------
    // link functions
    //------------------------------------------------------------
   
}
