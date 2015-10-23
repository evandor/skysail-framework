package io.skysail.product.todos.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.server.testsupport.SkysailServerRule;
import io.skysail.server.testsupport.categories.LargeTests;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.*;

import org.junit.*;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(SerenityRunner.class)
@Category(LargeTests.class)
public class TodosProductLargeTests {

    @Managed(driver = "firefox")
    private static WebDriver driver;

    @Rule
    public final SkysailServerRule serverRule = new SkysailServerRule("todos.largetest.bndrun" );

    @Steps
    BrowserSteps browserSteps;

    public TodosProductLargeTests() {
       System.setProperty("webdriver.firefox.bin", "C:\\Users\\graefca\\AppData\\Local\\Mozilla Firefox\\firefox.exe");
    }

    @Before
    public void setUp() {
        browserSteps.setDriver(driver);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        driver.quit();
    }

    @Test
    public void admin_can_login_and_logout_successfully() {
        browserSteps.loginAs("admin","skysail");
        assertThat(driver.getPageSource(), containsString("/Todos/v2"));

        browserSteps.logout();
        assertThat(driver.getPageSource(), not(containsString("/Todos/v2")));

//        driver.findElement(By.linkText("Todos")).click();
//        System.out.println(driver.getPageSource());
    }

    @Test
    public void admin_cannot_login_with_wrong_credentials() {
        browserSteps.loginAs("admin","xxx");
        assertThat(driver.getPageSource(), not(containsString("/Todos/v2")));
    }
}
