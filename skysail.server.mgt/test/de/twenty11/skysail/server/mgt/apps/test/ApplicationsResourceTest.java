//package de.twenty11.skysail.server.mgt.apps.test;
//
//import static org.hamcrest.Matchers.equalTo;
//import static org.hamcrest.Matchers.is;
//import static org.junit.Assert.assertThat;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import org.mockito.Mockito;
//import org.restlet.Application;
//
//import de.twenty11.skysail.server.mgt.ManagementApplication;
//import de.twenty11.skysail.server.mgt.apps.ApplicationDescriptor;
//import de.twenty11.skysail.server.mgt.apps.ApplicationsResource;
//import de.twenty11.skysail.server.restlet.SkysailApplication;
//import de.twenty11.skysail.server.services.OsgiServicesProvider;
//import de.twenty11.skysail.server.validation.ResourceTestWithUnguardedAppication;
//
//public class ApplicationsResourceTest extends ResourceTestWithUnguardedAppication<ManagementApplication> {
//
//    @BeforeClass
//    public static void before() {
//        // deleteTestDatabase();
//        // createInitialTestDatabase();
//    }
//
//    private ApplicationsResource applications;
//
//    @Before
//    public void setUp() throws Exception {
//        spy = (ManagementApplication) setUpMockedApplication(new ManagementApplication());
//        applications = new ApplicationsResource();
//        // setUpRequestAndResponse();
//        // spy.setEntityManagerFactory(getEmfForTests2("UserManagementPU"));
//    }
//
//    @Test
//    public void returns_empty_list_when_there_is_no_osgiServiceProvider() {
//        List<ApplicationDescriptor> data = applications.getData();
//        assertThat(data.size(), is(0));
//    }
//
//    @Test
//    public void returns_app_list_when_osgiServiceProvider_is_present() {
//        OsgiServicesProvider osgiServiceProvider = Mockito.mock(OsgiServicesProvider.class);
//        Set<Application> appSet = new HashSet<Application>();
//        SkysailApplication app = new SkysailApplication("appName") {
//
//            @Override
//            protected void attach() {
//            }
//
//        };
//        appSet.add(app);
//        Mockito.when(osgiServiceProvider.getApplications()).thenReturn(appSet);
//        Mockito.when(spy.getOsgiServicesProvider()).thenReturn(osgiServiceProvider);
//
//        List<ApplicationDescriptor> data = applications.getData();
//
//        assertThat(data.size(), is(1));
//        assertThat(data.get(0).getName(), is(equalTo("appName")));
//    }
//
//}
