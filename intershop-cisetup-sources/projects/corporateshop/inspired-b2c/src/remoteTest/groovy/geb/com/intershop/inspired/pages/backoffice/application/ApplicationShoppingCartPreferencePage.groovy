package geb.com.intershop.inspired.pages.backoffice.application

import geb.Page

class ApplicationShoppingCartPreferencePage extends Page
{
    static at=
    {
      waitFor {contentSlot.size() > 0}
    }
    
    static content =
    {
        contentSlot {$('form[name="ApplicationPreferenceGeneral"]')}
    }
    
    def applyBreakdown()
    {
        interact
        {
            $("select",name:"ApplicationBasketPreference_BasketDisplayTaxesAndFees").value("2")
            $("input",name:"update").click()
        }
    }
    
    /*
     * applies consolidated
     */
    def applyDefault()
    {
        interact
        {
            $("select",name:"ApplicationBasketPreference_BasketDisplayTaxesAndFees").value("1")
            $("input",name:"update").click()
        }
    }
    
    def setBasketMinTotalValue(value){
        when: "set min item totals"
        waitFor{$("input",id:"ApplicationBasketPreference_BasketMinTotalValue_EUR").size()>0}
        $("input",id:"ApplicationBasketPreference_BasketMinTotalValue_EUR").value value
        $("input",id:"ApplicationBasketPreference_BasketMinTotalValue_USD").value value
        then: "...and click Apply."
        $("input",name:"update").click()
        
        then: "Done... Back to Test."
        waitFor{$("input",id:"ApplicationBasketPreference_BasketMinTotalValue_EUR").size()>0}
    }

}
