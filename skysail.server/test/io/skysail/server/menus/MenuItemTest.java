package io.skysail.server.menus;

import io.skysail.server.app.SkysailApplication;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.mockito.Mockito;

import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import de.twenty11.skysail.server.services.test.TestServerResource;

public class MenuItemTest {

    TestServerResource myResource = new TestServerResource();
    private SkysailApplication app;
    private List<RouteBuilder> routeBuilders;
    private RouteBuilder defaultRouteBuilder;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        app = Mockito.mock(SkysailApplication.class);
        Mockito.when(app.getName()).thenReturn("testapp");
        routeBuilders = new ArrayList<>();

        defaultRouteBuilder = new RouteBuilder("/path", TestServerResource.class);
        routeBuilders.add(defaultRouteBuilder);
    }
    
    
//    @Test
//    public void sets_linkpath_to_nameOfApp_plus_configured_pathFromRoute() throws Exception {
//        Mockito.when(app.getRouteBuilders(TestServerResource.class)).thenReturn(routeBuilders);
//        MenuItem item = new MenuItem(app, TestServerResource.class);
//        assertThat(item.getLink(), is(equalTo("/testapp/path")));   
//    }
//    
//    @Test
//    public void sets_name_to_defaultRelation_if_title_is_not_given() throws Exception {
//        Mockito.when(app.getRouteBuilders(TestServerResource.class)).thenReturn(routeBuilders);
//        MenuItem item = new MenuItem(app, TestServerResource.class);
//        assertThat(item.getName(), is("unknown"));
//    }
    
//    @Test
//    public void sets_securedByRole_to_null_by_default() throws Exception {
//        Mockito.when(app.getRouteBuilders(TestServerResource.class)).thenReturn(routeBuilders);
//        MenuItem item = new MenuItem(app, TestServerResource.class);
//        assertThat(item.getSecuredByRole(), is(nullValue()));
//    }
//    
//    @Test
//    public void sets_category_to_ADMIN_by_default() throws Exception {
//        Mockito.when(app.getRouteBuilders(TestServerResource.class)).thenReturn(routeBuilders);
//        MenuItem item = new MenuItem(app, TestServerResource.class);
//        assertThat(item.getCategory(), is(equalTo(Category.ADMIN_MENU)));
//    }
    
//    @Test
//    public void sets_authenticationNeed_to_true_by_default_for_all_constructors() throws Exception {
//        Mockito.when(app.getRouteBuilders(TestServerResource.class)).thenReturn(routeBuilders);
//
//        MenuItem item = new MenuItem(app, TestServerResource.class);
//        assertThat(item.getNeedsAuthentication(), is(true));
//
//        item = new MenuItem("name", "link");
//        assertThat(item.getNeedsAuthentication(), is(true));
//
//        item = new MenuItem(null, "name", "link");
//        assertThat(item.getNeedsAuthentication(), is(true));
//        
//        item = new MenuItem("name", "link", app);
//        assertThat(item.getNeedsAuthentication(), is(true));
//    }
}
