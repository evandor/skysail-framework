package io.skysail.server.app.todos.test;

import static org.openqa.selenium.support.ui.ExpectedConditions.titleContains;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Managed;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

@RunWith(SerenityRunner.class)
public class WhenSearchingOnGoogle {

    @Managed(driver = "htmlunit")
    WebDriver driver;

    @Test
    public void shouldInstantiateAWebDriverInstanceForAWebTest() {
        driver.get("http://www.google.com");

        driver.findElement(By.name("q")).sendKeys("firefly", Keys.ENTER);

       new WebDriverWait(driver,5).until(titleContains("Google Search"));

        //assertThat(driver.getTitle()).isEqualTo("firefly - Google Search");
    }
}