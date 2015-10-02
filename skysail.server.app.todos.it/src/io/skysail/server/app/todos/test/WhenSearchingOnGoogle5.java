package io.skysail.server.app.todos.test;

import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Managed;

import org.junit.*;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import aQute.bnd.build.*;

@RunWith(SerenityRunner.class)
@Slf4j
public class WhenSearchingOnGoogle5 {

    private static Workspace ws;

    @Managed(driver = "firefox")
    private static WebDriver driver;

    private static Project project;
    private static ProjectLauncher bndLauncher;
    private static Run run;
    private static Thread launcherThread;

    public WhenSearchingOnGoogle5() {
        System.setProperty("webdriver.firefox.bin",
                "C:\\Users\\graefca\\AppData\\Local\\Mozilla Firefox\\firefox.exe");
    }

    @BeforeClass
    public static void s() throws Exception {
//        ws = new Workspace(IO.getFile("../"));
//        project = ws.getProject("skysail.product.todos");
//        project.setTrace(true);
//        run = Workspace.getRun(IO.getFile("todos.test.bndrun"));
//        bndLauncher = run.getProjectLauncher();
//        bndLauncher.setTrace(true);
//        bndLauncher.prepare();
//
//        launcherThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    bndLauncher.launch();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }, "Test Launcher");

        //launcherThread.start();

//        File pathToBinary = new File("C:\\Users\\graefca\\AppData\\Local\\Mozilla Firefox\\firefox.exe");
//        FirefoxBinary ffBinary = new FirefoxBinary(pathToBinary);
//        FirefoxProfile firefoxProfile = new FirefoxProfile();
//        driver = new FirefoxDriver(ffBinary,firefoxProfile);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        launcherThread.stop();
        run.close();
        project.close();
        ws.close();
    }

    @Before
    public void setUp() {
        // ((HtmlUnitDriver)driver).setHTTPProxy("192.168.11.140", 8080, new
        // ArrayList<>());
        // driver.
        System.out.println("hier");
        // Capabilities capabilities =
        // ((WebDriverFacade)driver).getCapabilities();
        // WebDriver proxiedDriver =
        // ((WebDriverFacade)driver).getProxiedDriver();
        // Options manage = ((WebDriverFacade)driver).manage();
        // HtmlUnitDriver htmlUnitDriver = (HtmlUnitDriver)
        // ((WebDriverFacade)driver).getProxiedDriver();
        // htmlUnitDriver.

    }

    @Test
    public void shouldInstantiateAWebDriverInstanceForAWebTest() {
        driver.get("http://localhost:2015/_login");
        System.out.println(driver.getPageSource());
        // WebElement findElement = driver.findElement(By.name("q"));
        //
        // System.out.println(findElement);
        // driver.findElement(By.name("q")).sendKeys("firefly", Keys.ENTER);
        //
        // new WebDriverWait(driver,5).until(titleContains("Google Search"));

        // assertThat(driver.getTitle()).isEqualTo("firefly - Google Search");
    }
}