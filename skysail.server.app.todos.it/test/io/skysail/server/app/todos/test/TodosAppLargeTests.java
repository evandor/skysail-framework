package io.skysail.server.app.todos.test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Managed;

import org.junit.*;
import org.junit.runner.RunWith;
import org.openqa.selenium.*;

import aQute.bnd.build.*;
import aQute.lib.io.IO;

@RunWith(SerenityRunner.class)
public class TodosAppLargeTests {

    @Managed(driver = "firefox")
    private static WebDriver driver;

    private static Workspace ws;
    private static Project project;
    private static ProjectLauncher bndLauncher;
    private static Run run;
    private static Thread launcherThread;

    public TodosAppLargeTests() {
        System.setProperty("webdriver.firefox.bin", "C:\\Users\\graefca\\AppData\\Local\\Mozilla Firefox\\firefox.exe");
    }

    @BeforeClass
    public static void s() throws Exception {
        ws = new Workspace(IO.getFile("../"));
        project = ws.getProject("skysail.product.todos");
        run = Workspace.getRun(IO.getFile("todos.test.bndrun"));
        bndLauncher = run.getProjectLauncher();
        bndLauncher.prepare();

        launcherThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    bndLauncher.launch();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "Test Launcher");

        launcherThread.start();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        driver.quit();
        launcherThread.stop();
        run.close();
        project.close();
        ws.close();
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
