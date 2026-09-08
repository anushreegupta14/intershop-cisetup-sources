import org.openqa.selenium.Dimension
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.firefox.*
import org.openqa.selenium.remote.*

def gebEnv = System.getProperty("geb.env");
def webDriverDir = System.getProperty("webDriverDir")
def webDriverExec = new File(webDriverDir, System.getProperty("webDriverExec")).absolutePath

waiting {
	// max request time in seconds
    timeout = 20
    
    // http://gebish.org/manual/current/#failure-causes
    includeCauseInMessage = true
}

environments {

    chromeTablet {
        driver = {
            def driver = createChromeDriverInstance(webDriverExec)
            driver.manage().window().setSize(new Dimension(1024, 768))
            driver
        }
    }

    chromePC {
        driver = {
            def driver = createChromeDriverInstance(webDriverExec)
            driver.manage().window().setSize(new Dimension(1920, 1200))
            driver
        }
    }

    geckoPC {
        driver = {
            def driver = createGeckoDriverInstance(webDriverExec)
            driver.manage().window().setSize(new Dimension(1920, 1200))
            driver
        }
    }

    geckoTablet {
        driver = {
            def driver = createGeckoDriverInstance(webDriverExec)
            driver.manage().window().setSize(new Dimension(1024, 768))
            driver
        }
    }
}

private def createGeckoDriverInstance(String webDriverExec) {
    System.setProperty("webdriver.gecko.driver", webDriverExec)
    FirefoxOptions options = new FirefoxOptions()
    options.setHeadless(true)
    options.setLogLevel(FirefoxDriverLogLevel.ERROR)
    driverInstance = new FirefoxDriver(options)
    driverInstance
}

private def createChromeDriverInstance(String webDriverExec) {
    System.setProperty("webdriver.chrome.driver", webDriverExec)
    driverInstance = new ChromeDriver()
    driverInstance
}





