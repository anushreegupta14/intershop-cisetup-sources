package geb.com.intershop.inspired.specs.storefront.b2c.responsive

import geb.com.intershop.inspired.pages.storefront.responsive.HomePage
import geb.com.intershop.inspired.testdata.TestDataUsage
import geb.spock.GebReportingSpec



/**
 * Storefront tests Short Links handling
 *
    test short links:
    - call the short links predefined in a_responsive/demo_responsive/staticfiles/share/sites/inSPIRED-inTRONICS-Site/units/inSPIRED-inTRONICS/impex/src/ShortLinks.xml
      and check for the correct HTTP return codes 200, 301, 302
    - checks the success of redirection  

 */
class SEOShortLinksSpec extends GebReportingSpec implements TestDataUsage
{

    /**
     * Check that response code is as configured, when not automatically following redirects
     * 
     */
    def "Test ShortLink redirects behaviour without following redirects"()
    {
        def i = 0
        
        3.times{
            def responseStatus = -1

            when: "I call a Short Link, but do not automatically follow redirection."
                responseStatus = getResponseCode(makeURL(shortLink[i]))
            then: "The pre-defined HTTP response code is received."
                assert responseStatus == shortLinkReturn[i]
            
            i += 1
        }
        
        where:
            shortLink       = testData.get("seo.shortLink.link")
            shortLinkReturn = testData.get("seo.shortLink.returnCode")
    }

    /**
     * Check that response code is HTTP 200, when following redirects, and that each given ShortLink
     * leads to the inTRONICS home page
     * 
     */
    def "Test ShortLink redirects behaviour when following redirects"() 
    {
        
        def i = 0

        3.times{
            when: "I call a Short Link and automatically follow redirection."
                go makeURL(shortLink[i])
            then: "The Short Link leads to the Homepage."
                at HomePage
            
            i += 1
        }
        
        where:
            shortLink       = testData.get("seo.shortLink.link")
    }
    

    /**
     * Create the complete URL string from a given page path.
     * Uses the current browser used by the Spec and it's defined base URL.
     *     
     * @param   pagePath  The page path
     * @return  String    The complete URL string
     */
    private String makeURL(String pagePath) {
        return getBrowser().getBaseUrl() + pagePath
    }
    
    /**
     * Get the HTTP response code, which is responded for the given URL string.
     * The method uses a HTTPConnection, which does NOT automatically follow redirections.
     *   
     * @param urlString The URL string
     * @return  int     The HTTP response code before redirection, or -1 in case of an error.
     * @throws MalformedURLException
     */
    private static int getResponseCode(String urlString)
            throws MalformedURLException 
    {
        URL url = new URL(urlString);
        try {
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection huc = (HttpURLConnection)url.openConnection();
            
            huc.setRequestMethod("GET");
            huc.connect();
            
            return huc.getResponseCode();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
            return -1;
        }
        finally {
            HttpURLConnection.setFollowRedirects(true);
        }
    }
    
}
