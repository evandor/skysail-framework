package io.skysail.server.app.todos.test;

import org.junit.*;

//@RunWith(SerenityRunner.class)
public class WhenSearchingOnGoogle2 {

//    @Managed(driver = "htmlunit")
//    WebDriver driver;

    @BeforeClass
    public static void s() {
        System.out.println("hier");
    }
    @Before
    public void setUp() {
        //((HtmlUnitDriver)driver).setHTTPProxy("192.168.11.140", 8080, new ArrayList<>());
        //driver.
        System.out.println("hier");
//        Capabilities capabilities = ((WebDriverFacade)driver).getCapabilities();
//        WebDriver proxiedDriver = ((WebDriverFacade)driver).getProxiedDriver();
//        Options manage = ((WebDriverFacade)driver).manage();
//        HtmlUnitDriver htmlUnitDriver = (HtmlUnitDriver) ((WebDriverFacade)driver).getProxiedDriver();
        //htmlUnitDriver.

    }

    @Test
    public void shouldInstantiateAWebDriverInstanceForAWebTest() {
       // driver.get("http://localhost:2015/_login");
        System.out.println("driver.getPageSource()");
//        WebElement findElement = driver.findElement(By.name("q"));
//
//        System.out.println(findElement);
//        driver.findElement(By.name("q")).sendKeys("firefly", Keys.ENTER);
//
//       new WebDriverWait(driver,5).until(titleContains("Google Search"));

        //assertThat(driver.getTitle()).isEqualTo("firefly - Google Search");
    }


}