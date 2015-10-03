//package io.skysail.server.app.todos.test;
//
//import lombok.extern.slf4j.Slf4j;
//import net.thucydides.core.annotations.Managed;
//
//import org.junit.*;
//import org.openqa.selenium.WebDriver;
//
//import aQute.bnd.build.*;
//import aQute.lib.io.IO;
//
////@RunWith(SerenityRunner.class)
//@Slf4j
//public class WhenSearchingOnGoogle3 {
//
//    private static Workspace ws;
//
//    @Managed(driver = "chrome")
//    WebDriver driver;
//
//    private static Project project;
//    private static ProjectLauncher bndLauncher;
//    private static Run run;
//    private static Thread launcherThread;
//
//    @BeforeClass
//    public static void s() throws Exception {
//        ws = new Workspace(IO.getFile("../"));
//        project = ws.getProject("skysail.product.todos");
//        run = Workspace.getRun(IO.getFile("todos.test.bndrun"));
//        bndLauncher = run.getProjectLauncher();
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
//
//        launcherThread.start();
//    }
//
//    @AfterClass
//    public static void tearDown() throws Exception {
//        launcherThread.stop();
//        run.close();
//        project.close();
//        ws.close();
//    }
//
//    @Before
//    public void setUp() {
//        // ((HtmlUnitDriver)driver).setHTTPProxy("192.168.11.140", 8080, new
//        // ArrayList<>());
//        // driver.
//        System.out.println("hier");
//        // Capabilities capabilities =
//        // ((WebDriverFacade)driver).getCapabilities();
//        // WebDriver proxiedDriver =
//        // ((WebDriverFacade)driver).getProxiedDriver();
//        // Options manage = ((WebDriverFacade)driver).manage();
//        // HtmlUnitDriver htmlUnitDriver = (HtmlUnitDriver)
//        // ((WebDriverFacade)driver).getProxiedDriver();
//        // htmlUnitDriver.
//
//    }
//
//    @Test
//    public void shouldInstantiateAWebDriverInstanceForAWebTest() {
//        driver.get("http://localhost:2015/_login");
//        System.out.println(driver.getPageSource());
//        // WebElement findElement = driver.findElement(By.name("q"));
//        //
//        // System.out.println(findElement);
//        // driver.findElement(By.name("q")).sendKeys("firefly", Keys.ENTER);
//        //
//        // new WebDriverWait(driver,5).until(titleContains("Google Search"));
//
//        // assertThat(driver.getTitle()).isEqualTo("firefly - Google Search");
//    }
//}