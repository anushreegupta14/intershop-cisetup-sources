package geb.com.intershop.inspired.pages.backoffice.product

import geb.Page
import geb.com.intershop.inspired.pages.backoffice.BOLoginPage

class ProductDetailsClassificationDetailsPage extends ProductDetailsPage
{

    static at =
    {
        waitFor{ contentSlot.size()>0 }

        waitFor{classificationForm.size()>0}
        waitFor{classificationSelector.size()>0}
    }

    static content= {

        contentSlot                 { $('table[data-testing-id^="page-bo-product-classification-details"]') }

        classificationForm			{ $("form", name:contains("productClassification_")) }
        classificationSelector		{ classificationForm.$("select",name:"SelectedCategoryID") }
		
        attributeForm               { $("form", name:"attributeForm") }

        btnSelect                   { classificationForm.$('input', name: 'apply') }
        btnApplyAdditionalValues    { attributeForm.$('input', name: 'update') }
	}

    def clickSelect()
    {
        btnSelect.click()
        waitFor{btnApplyAdditionalValues.displayed}
    }

    def clickApplyAdditionalValues()
    {
        btnApplyAdditionalValues.click()
    }

	def selectClassificationByName (cName)
	{
        classificationForm.click()
		classificationSelector.find("option").find{ 
            it.text().trim() == cName.trim()
        }.click()
	}

}

		
		
		
