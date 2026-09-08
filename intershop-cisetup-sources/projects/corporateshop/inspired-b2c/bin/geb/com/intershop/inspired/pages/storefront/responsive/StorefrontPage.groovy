package geb.com.intershop.inspired.pages.storefront.responsive

import geb.Page
import geb.com.intershop.inspired.pages.storefront.responsive.modules.Header

class StorefrontPage extends Page
{
    private static final String urlPrefix =  "INTERSHOP/web/WFS";
    private static final String urlSite =  "inSPIRED-inTRONICS-Site";
    private static final String urlLocale =  "en_US";
    private static final String urlApplication =  "-";
    private static final String urlCurrency =  "USD";

    //StorefrontPage is an abstract page for all Pages to have in store
    //Header, Footer and basic functionalities
     static url=  "/" + urlPrefix + 
                  "/" + urlSite + 
                  "/" + urlLocale +
                  "/" + urlApplication +
                  "/" + urlCurrency +
                  "/";
    
    static content =
    {
        header {$('header[data-testing-id="page-header"]').module(Header)}
    }

    static at =
    {
        waitFor{$('header[data-testing-id="page-header"]').size() > 0}
    }


    def sleepForNSeconds(int n)
    {
        Thread.sleep(n * 1000)
    }

    String metaDescription() {
        return $("meta[name='description']").@content
    }
    
    String metaKeywords() {
        return $("meta[name='keywords']").@content
    }
    
    String metaRobots() {
        return $("meta[name='robots']").@content
    }
	
    def linkRelNext() {
        return $("link[rel='next']")
    }

    def linkRelPrev() {
        return $("link[rel='prev']")
    }
}
