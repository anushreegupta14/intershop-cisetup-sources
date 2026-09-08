package geb.com.intershop.inspired.pages.storefront.responsive.checkout

import org.openqa.selenium.StaleElementReferenceException;

import geb.com.intershop.inspired.pages.storefront.responsive.StorefrontPage;
import geb.com.intershop.inspired.pages.storefront.responsive.modules.OrderAddressSummary;
import geb.com.intershop.inspired.pages.storefront.responsive.modules.ProgressBar;


class MultipleAddressPage extends StorefrontPage
{
    static at =
    {
        waitFor { contentSlot.present }
    }

    static content =
    {
        contentSlot         { $("div[data-testing-id='multiple-shipment-address-page']") }
        waitFor             { contentSlot.present }
        addAddressButton    { contentSlot.$('a',text:"Add address") }
        continueButton      { contentSlot.$('a',name: "continue") }
        orderAddressSummary { module OrderAddressSummary }
        navigationBar {module ProgressBar,$('div[data-testing-id="progress-bar"]')}
    }

    //------------------------------------------------------------
    // link functions
    //------------------------------------------------------------
    def addShippingAddress(country,fName,lName,address,city,postal)
    {
        addAddressButton.click()
        waitFor { $("div", "data-testing-id":"multiple-shipping-newaddress").displayed }
        contentSlot.$('select',name:"newaddress_CountryCode").value(country)
        waitFor { $("select", "name":"newaddress_Title").displayed }
        contentSlot.$('input',name:"newaddress_FirstName").value fName
        contentSlot.$('input',name:"newaddress_LastName").value lName
        contentSlot.$('input',name:"newaddress_Address1").value address
        contentSlot.$('input',name:"newaddress_City").value city
        contentSlot.$('input',name:"newaddress_PostalCode").value postal
        contentSlot.$('button',name:"addNewBilling").click()
        waitFor { !$("div", "data-testing-id":"multiple-shipping-newaddress").displayed }
    }
    
    def setProductAddress(param, fName, lName, address)
    {
        def result = false
        def attempts = 0
        // try multiple times because page is dynamic
        while (attempts < 2)
        {
            try
            {
                $('select[data-testing-id="multiple-address-select-'+param+'"]').value fName+" "+lName+" | "+address
                result = true
                break
            }
            catch(StaleElementReferenceException e)
            {
                // ignore
            }
            attempts++
        }
        return result
    }
    
    def continueClick()
    {
        continueButton.click()
    }

}
