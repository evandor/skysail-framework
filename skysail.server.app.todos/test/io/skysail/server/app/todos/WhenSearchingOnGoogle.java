//package io.skysail.server.app.todos;
//
//import net.thucydides.core.annotations.Managed;
//
//import org.junit.Test;
//
//public class WhenSearchingOnGoogle {
//
//    @Managed
//    WebDriver driver;
//
//    @Test
//    public void shouldInstantiateAWebDriverInstanceForAWebTest() {
//        driver.get("http://www.google.com");
//
//        driver.findElement(By.name("q")).sendKeys("firefly", Keys.ENTER);
//
//        new WebDriverWait(driver, 5).until(titleContains("Google Search"));
//
//        assertThat(driver.getTitle()).isEqualTo("firefly - Google Search");
//    }
//}
