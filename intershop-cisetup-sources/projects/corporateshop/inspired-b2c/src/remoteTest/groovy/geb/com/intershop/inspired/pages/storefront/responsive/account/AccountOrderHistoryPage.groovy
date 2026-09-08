package geb.com.intershop.inspired.pages.storefront.responsive.account

import geb.com.intershop.inspired.pages.storefront.responsive.StorefrontPage;

class AccountOrderHistoryPage  extends StorefrontPage
{
    static url= StorefrontPage.url + "ViewOrders-SimpleOrderSearch";
    
    static at=
    {
        waitFor{contentSlot.size()>0}
    }

    static content=
    {
        contentSlot { $("div[data-testing-id='orders-history-page']") }
    }
    
    def checkOrderHistoryLine(param)
    {
        $("div",class:"list-item-row").find("div", text:param).size() <= 0
    }

}
