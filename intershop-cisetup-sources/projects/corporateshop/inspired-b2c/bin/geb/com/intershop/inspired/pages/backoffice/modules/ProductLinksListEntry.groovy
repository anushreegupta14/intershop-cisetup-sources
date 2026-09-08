package geb.com.intershop.inspired.pages.backoffice.modules

import geb.Module;
import geb.navigator.Navigator;

class ProductLinksListEntry extends Module
{
    static content = {
        entry       { i -> $('td', i) }
        
        selected    { entry(0).find("input", name:"SelectedProductUUID") }
        pname       { entry(1).text().trim() }
        sku         { entry(2).text().trim() }
    }
}