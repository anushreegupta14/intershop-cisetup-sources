package geb.com.intershop.b2x.pages.storefront.responsive.contract

import org.testng.Assert

import geb.com.intershop.b2x.pages.storefront.responsive.account.AccountPage
import geb.com.intershop.b2x.pages.storefront.responsive.contract.modules.ContractOrderRow

class AccountContractDetailsPage extends AccountPage {
    
    String pageId() {"b2b-account-contract-details"}

    static at =
    {
        waitFor{ $("div", "data-testing-id": "b2b-account-contract-details") }
    }
    
    static content = {
        orders { $('div[data-testing-id="b2b-account-contract-details-orders"] table tbody tr').moduleList(ContractOrderRow) }
    }
    
    boolean checkContract(String currentRevenue)
    {
        //TODO improve check            
        def divOrders = $("div[data-testing-id='b2b-account-contract-details-orders']")                                    
        Assert.assertEquals(divOrders.size(), 1, "No order found for contract.")
        def order = orders.find { contractOrder ->
            println contractOrder.contractRevenue + " || " + currentRevenue + " --> " + contractOrder.contractRevenue.endsWith(currentRevenue)
            contractOrder.contractRevenue.endsWith(currentRevenue)
        }
        println "found order = " + order
        return order.size() == 1
//        assert $("dd[data-testing-id='b2b-account-contract-details-current-revenue']").text().endsWith(" " + currentRevenue)
    }
}