package geb.com.intershop.inspired.pages.backoffice.preferences


import geb.Page

class PricingPreferencesPage extends Page
{
    static at=
    {
      waitFor{$("select",name:"PricingPreferencesForm_PriceType").size()>0}
    }
    
    def applyNet()
    {
        //"Price Display for Individual Customers" to "Net"
        applyPriceTypeAndDisplay("net", "net")
    }

    def applyDefault()
    {
        //"Price Display for Individual Customers" to "Gross"
        applyPriceTypeAndDisplay("gross", "gross")
    }
    
    def applyPriceTypeAndDisplay(String priceType, String priceDisplay)
    {
        interact
        {
            $("select",name:"PricingPreferencesForm_PriceType").value(priceType)
            $("select",name:"CustomerTypePricingPreferencesForm_PRIVATE_PriceDisplayType").value(priceDisplay)
            $("input",name:"updateSettings").click()
        }
    }    
    
    
}
