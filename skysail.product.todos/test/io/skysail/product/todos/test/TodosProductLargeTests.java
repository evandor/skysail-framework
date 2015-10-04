package io.skysail.product.todos.test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import io.skysail.server.testsupport.*;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Managed;

import org.junit.*;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.*;

@RunWith(SerenityRunner.class)
@Category(LargeTests.class)
public class TodosProductLargeTests {

    @Managed(driver = "firefox")
    private static WebDriver driver;

    @Rule
    public final SkysailServerRule serverRule = new SkysailServerRule("todos.bndrun" );

    public TodosProductLargeTests() {
       // System.setProperty("webdriver.firefox.bin", "C:\\Users\\graefca\\AppData\\Local\\Mozilla Firefox\\firefox.exe");
    }

    @AfterClass
    public static void tearDown() throws Exception {
        driver.quit();
    }

    @Test
    public void admin_can_login_successfully() {
        driver.get("http://127.0.0.1:2015/_login");
        driver.findElement(By.name("username")).sendKeys("admin");
        WebElement passwordElement = driver.findElement(By.name("password"));
        passwordElement.sendKeys("skysail");
        passwordElement.submit();
        System.out.println("Page title is: " + driver.getTitle());
        System.out.println(driver.getPageSource());
        assertThat(driver.getPageSource(), containsString("/Todos/v2"));
    }
}
