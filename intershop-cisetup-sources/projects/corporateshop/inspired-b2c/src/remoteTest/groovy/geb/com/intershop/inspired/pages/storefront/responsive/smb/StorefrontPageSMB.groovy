package geb.com.intershop.inspired.pages.storefront.responsive.smb

import geb.Page
import geb.com.intershop.inspired.pages.storefront.responsive.modules.Header

class StorefrontPageSMB extends Page
{
    //StorefrontPageSMB is an abstract page for setting and reference the url to smb
    static url= "/" + System.getProperty("url.prefix", "INTERSHOP/web/WFS/").trim() + 
                      System.getProperty("url.site.smb", "inSPIRED-inTRONICS_Business-Site").trim() + 
                "/" + System.getProperty("url.locale", "en_US").trim() +
                "/" + System.getProperty("url.application", "-").trim() +
                "/" + System.getProperty("url.currency" , "USD").trim() +
                "/";
                    
}
