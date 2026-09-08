package geb.com.intershop.inspired.pages.backoffice.product


class ProductDetailsClassificationListPage extends ProductDetailsPage
{
    static at =
    {
       waitFor{ contentSlot.size()>0 }
    }

    static content= {
	
        contentSlot                 { $('table[data-testing-id^="page-bo-product-classification-list"]') }

        linkServiceTypesAssign(to: ProductDetailsClassificationDetailsPage)      { $("a",href:contains("CategoryName=ServiceTypes") ) }
        linkProductTypesAssign      { $("a",href:contains("CategoryName=ProductTypes") ) }
        
	}

		
    def clickAssignServiceType()
    {
        linkServiceTypesAssign.click()

    }

    def clickAssignProductType()
    {
        linkProductTypesAssign.click()

    }

}

		
		
		
