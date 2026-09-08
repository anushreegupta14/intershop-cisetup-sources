package geb.com.intershop.inspired.pages.storefront.responsive.checkout.variation

import geb.com.intershop.inspired.pages.storefront.responsive.StorefrontPage;

class EditVariationModal extends StorefrontPage
{
    static at =
    {
        sleepForNSeconds(3)
        waitFor{saveVariationButton.size()>0}
    }

    static content =
    {
        saveVariationButton {$('button', text:'SAVE')}
        quantityInput { $('input[class="input-quantity form-control"]') }
        productVariationSelect {term -> module(new VariationRow(variation: term)) }
    }

    
    def sleepForNSeconds(int n)
    {
        Thread.sleep(n * 1000)
    }
}

