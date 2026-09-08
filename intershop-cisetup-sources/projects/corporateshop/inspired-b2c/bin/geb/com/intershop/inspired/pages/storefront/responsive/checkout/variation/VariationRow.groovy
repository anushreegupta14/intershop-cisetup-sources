package geb.com.intershop.inspired.pages.storefront.responsive.checkout.variation

import geb.Module;
import geb.navigator.Navigator;

class VariationRow extends Module
{
    def variation;
    
    static content = {
        variationSelection { $('select[data-variation-attribute="'+variation+'"]')}
    }
    
}
