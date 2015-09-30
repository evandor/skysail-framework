package io.skysail.server.app.todos.test;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.webdriver.WebDriverFacade;

import org.junit.*;
import org.junit.runner.RunWith;
import org.openqa.selenium.*;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

@RunWith(SerenityRunner.class)
public class WhenSearchingOnGoogle {

    @Managed(driver = "htmlunit")
    WebDriver driver;

    @Before
    public void setUp() {
        //((HtmlUnitDriver)driver).setHTTPProxy("192.168.11.140", 8080, new ArrayList<>());
        //driver.
        Capabilities capabilities = ((WebDriverFacade)driver).getCapabilities();
        WebDriver proxiedDriver = ((WebDriverFacade)driver).getProxiedDriver();
        Options manage = ((WebDriverFacade)driver).manage();
        HtmlUnitDriver htmlUnitDriver = (HtmlUnitDriver) ((WebDriverFacade)driver).getProxiedDriver();
        //htmlUnitDriver.

    }

    @Test
    public void shouldInstantiateAWebDriverInstanceForAWebTest() {
        driver.get("http://localhost:2015/_login");
        System.out.println(driver.getPageSource());
//        WebElement findElement = driver.findElement(By.name("q"));
//
//        System.out.println(findElement);
//        driver.findElement(By.name("q")).sendKeys("firefly", Keys.ENTER);
//
//       new WebDriverWait(driver,5).until(titleContains("Google Search"));

        //assertThat(driver.getTitle()).isEqualTo("firefly - Google Search");
    }


}