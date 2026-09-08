package geb.com.intershop.inspired.pages.backoffice.product;

public class ProductDetailsPricingPage extends ProductDetailsPage
{
    static at =
            {
                waitFor(10, 1){ contentSlot.size()>0 }
            }

    static content= {

        contentSlot                         { $('form[data-testing-id^="page-bo-product-pricing"]') }


        productPriceCurrencyForm            { contentSlot }
        productListPriceCurrencySelector    { productPriceCurrencyForm.$("select",name:"ListPriceCurrency_CurrencyMnemonic") }

        productListPriceValueInput          { productPriceCurrencyForm.$("input",name:"ListPriceValue_CurrencyMnemonic") }

        btnApplyListPriceValueUpdate        { productPriceCurrencyForm.$('input', name: 'apply') }
        btnAddListPriceEnty(to: ProductDetailsPricingPage)                 { productPriceCurrencyForm.$('input', name: 'addListPrice') }

    }


    def applyReduced()
    {
        lockProduct()
        interact
                {
                    $("select",id:"TaxClasses_Code").value("ReducedTax")
                    $("input",name:"updateTaxation").click()
                }
        unlockProduct()
    }

    def applyDefault()
    {
        lockProduct()
        interact
                {
                    $("select",id:"TaxClasses_Code").value("FullTax")
                    $("input",name:"updateTaxation").click()
                }
        unlockProduct()
    }

    def selectListPriceCurrencyByName (cName)
    {
        productPriceCurrencyForm.click()
        productListPriceCurrencySelector.find("option").find{
            it.text().trim() == cName.trim()
        }.click()
    }

    def setListPriceValue(listPriceValue)
    {
        productListPriceValueInput.value listPriceValue
    }

    def clickAddListPriceEntry()
    {
        btnAddListPriceEnty.click()

    }

    def clickApplyListPriceValueUpdate()
    {
        btnApplyListPriceValueUpdate.click()
    }

}
