package geb.com.intershop.inspired.pages.storefront.responsive.checkout

import geb.com.intershop.inspired.pages.storefront.responsive.StorefrontPage;
import geb.com.intershop.inspired.pages.storefront.responsive.modules.CartRow
import geb.com.intershop.inspired.pages.storefront.responsive.modules.OrderSummaryRow
import geb.com.intershop.inspired.pages.storefront.responsive.StorefrontPage

class CartPage extends StorefrontPage
{
    static url= StorefrontPage.url + "ViewCart-View";

    static at =
    {
        waitFor{ contentSlot.size()>0}
        waitFor{updateButton.size()>0}
    }

    static content =
    {
        contentSlot {$("div[data-testing-id='cart-page']")}
       
        updateButton { $("button",type:"submit",name:"update")[0]}
        checkoutButton {$("button",name:'checkout')}

        checkoutButtonFastPay {$('button', text:'CHECKOUT WITH ISH DEMO FAST PAY')}

        productCartTable  { term -> module(new CartRow(productTerm: term)) }

        price            { $('dt',text:"Subtotal").parent().find("dd",0).text().
            replaceAll(',','').find(/\d+(\.\d+)?/) as BigDecimal }

        OrderSummaryRow { term -> module(new OrderSummaryRow(fieldName: term)) }

        //quantityInput { $("input", class:"quantity") }
        
    }


    def checkOrderSummaryLine(param)
    {
        OrderSummaryRow(param).orderLine.size() > 0
    }

    //------------------------------------------------------------
    // Page checks
    //------------------------------------------------------------

    def checkProduct(param)
    {
        productCartTable(param).quantityInput.size() > 0
    }


    //------------------------------------------------------------
    // link functions
    //------------------------------------------------------------
    def checkOut()
    {
        checkoutButton.click()

    }

    def checkOutFastPay()
    {
        checkoutButtonFastPay.click();
    }
}
