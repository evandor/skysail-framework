package io.skysail.product.todos.test;

import lombok.Setter;
import net.thucydides.core.annotations.Step;

import org.openqa.selenium.*;

public class BrowserSteps {

    @Setter
    private WebDriver driver;

    @Step("logging in as user '{0}' with password '{1}'")
    public void loginAs(String username, String password) {
        driver.get("http://127.0.0.1:2099/_login");
        driver.findElement(By.name("username")).sendKeys(username);
        WebElement passwordElement = driver.findElement(By.name("password"));
        passwordElement.sendKeys(password);
        passwordElement.submit();
    }

    @Step
    public void logout() {
        driver.get("http://127.0.0.1:2099/_logout?targetUri=/");
    }


}
