package geb.com.intershop.inspired.specs.storefront.b2c.responsive

import geb.com.intershop.inspired.pages.backoffice.BOLoginPage
import geb.com.intershop.inspired.pages.backoffice.catalog.CatalogGeneralTab
import geb.com.intershop.inspired.pages.backoffice.catalog.CatalogListPage
import geb.com.intershop.inspired.pages.backoffice.preferences.PageCachingPreferencesPage
import geb.com.intershop.inspired.pages.storefront.responsive.HomePage
import geb.spock.GebReportingSpec
import spock.lang.Timeout


@Timeout(600)
class CatalogShowInMenuSpec extends GebReportingSpec 
{
    
    def cleanup()
    {
        when: "I go to the backoffice login..."
            to BOLoginPage
        then:
            at BOLoginPage
        when: "Login Backoffice"
            login "admin","!InterShop00!","inSPIRED"
        
        when:"Go to sales channel catalog list"
            toB2CSalesChannel()
        
            goToCatalogList()
        
        then:
            at CatalogListPage
        
        when:"Edit catalog Cameras"
            editCatalogByEditLink("Cameras")
        
        then:"On catalog general tab"
            at CatalogGeneralTab
         
        when:"set ShowInMenu to true"
            checkShowInMenuCheckbox()
            btnApply.click()
        
        then: "be sure the catalog is online" 
            checkBoxShowInMenu == "True"
    }
    
    def "Switch catalog ShowInMenu on/off"()
    {
        when: "I go to the backoffice..."
            to BOLoginPage
        then:
            at BOLoginPage

        when: "Login Backoffice"
            login "admin","!InterShop00!","inSPIRED"
        
        and: 
            toB2CSalesChannel()
           
        //Turn off Page cache
        then:
            waitFor{$("a", href: contains("ViewDomainPreference_52-Start"), class: "overview_subtitle").size()>0}
         when:
             $("a", href: contains("ViewDomainPreference_52-Start"), class: "overview_subtitle").click()
         then:
             waitFor{$("a", href: contains("ViewPageCache_52-Start"), class: "overview_subtitle").size()>0}
        when:
             $("a", href: contains("ViewPageCache_52-Start"), class: "overview_subtitle").click()
        then:
            at PageCachingPreferencesPage
        when:
            uncheckPageCachingAllowed()
            btnApply.click()
        then:
            waitFor{$("a", href: contains("ViewOverview-Channel"), class: "breadcrumb").size()>0}
        when:
            $("a", href: contains("ViewOverview-Channel"), class: "breadcrumb").click()
            
            goToCatalogList()      
        then:
            at CatalogListPage
        
        when: "Edit Cameras catalog"
            editCatalogByEditLink("Cameras")
       
        then:"On catalog general tab"
            at CatalogGeneralTab
        
        when:"set ShowInMenu to true"
            checkShowInMenuCheckbox()
            btnApply.click()
        
        and: "I open storefront"
            to HomePage
        
        then: "Cameras catalog is visible"
            $('[data-testing-id="Cameras-Camcorders-link"]').size()>0;
        
        when: "I go to the backoffice..."
            to BOLoginPage

        then: "Login Backoffice"
            login "admin","!InterShop00!","inSPIRED"        
        
        when:"Go to sales channel catalog list"
            toB2CSalesChannel()
            goToCatalogList()
        
        then:
            at CatalogListPage
        
        when: "Edit Cameras catalog..."
            editCatalogByEditLink("Cameras")
        
        then:
            at CatalogGeneralTab
        
        when:"... and set ShowInMenu to false"
            uncheckShowInMenuCheckbox()
            btnApply.click()
        
        then: "I open storefront"
            to HomePage
        
        and:" assert Cameras catalog is not visible"
            $('[data-testing-id="Cameras-Camcorders-link"]').size()==0;
    }
    
    void goToCatalogList()
    {
        waitFor{$("a", href: contains("ViewOverview-ChannelCatalog"), class: "overview_subtitle").size()>0}
        $("a", href: contains("ViewOverview-ChannelCatalog"), class: "overview_subtitle").click()

        waitFor{$("a", href: contains("ViewChannelCatalogList_52-ListAll"), class: "overview_subtitle").size()>0}
        $("a", href: contains("ViewChannelCatalogList_52-ListAll"), class: "overview_subtitle").click()
    }
    
    
}
