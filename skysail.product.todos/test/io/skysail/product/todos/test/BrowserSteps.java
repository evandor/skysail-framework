package io.skysail.product.todos.test;

import lombok.Setter;
import net.thucydides.core.annotations.Step;

import org.openqa.selenium.*;

public class BrowserSteps {

    @Setter
    private WebDriver driver;

    @Step("logging in as user '{0}' with password '{1}'")
    public BrowserSteps loginAs(String username, String password) {
        driver.get("http://127.0.0.1:2099/_login");
        driver.findElement(By.name("username")).sendKeys(username);
        WebElement passwordElement = driver.findElement(By.name("password"));
        passwordElement.sendKeys(password);
        passwordElement.submit();
        return this;
    }

    @Step
    public BrowserSteps logout() {
        driver.get("http://127.0.0.1:2099/_logout?targetUri=/");
        return this;
    }

    public BrowserSteps navigateToPostTodo() {

        return this;
    }


}
