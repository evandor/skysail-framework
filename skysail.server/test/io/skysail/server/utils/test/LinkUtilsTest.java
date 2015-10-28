package io.skysail.server.utils.test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import io.skysail.api.domain.Identifiable;
import io.skysail.api.links.*;
import io.skysail.server.app.*;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.utils.LinkUtils;

import java.util.*;

import org.junit.*;
import org.mockito.Mockito;

import de.twenty11.skysail.server.core.restlet.RouteBuilder;

public class LinkUtilsTest {

    private SkysailApplication app;
    private List<RouteBuilder> routeBuilderList;

    @Before
    public void setUp() throws Exception {
        app = Mockito.mock(SkysailApplication.class);
        Mockito.when(app.getName()).thenReturn("testapp");
        Mockito.when(app.getApiVersion()).thenReturn(new ApiVersion(1));
        routeBuilderList = new ArrayList<>();
        RouteBuilder routeBuilder = Mockito.mock(RouteBuilder.class);
        Mockito.when(routeBuilder.getPathTemplate(new ApiVersion(1))).thenReturn("/path");
        routeBuilderList.add(routeBuilder);
    }

    @Test
    public void unknown_routeBuilder_yields_null_link() {
        Link link = LinkUtils.fromResource(app, TestListServerResource.class, "title");
        assertThat(link, is(nullValue()));
    }

    @Test
    public void default_setup_without_title_yields_expected_values_on_link() {
        Mockito.when(app.getRouteBuilders(TestListServerResource.class)).thenReturn(routeBuilderList);
        Link link = LinkUtils.fromResource(app, TestListServerResource.class);
        assertThat(link.getUri(), is("/testapp/path"));
        assertThat(link.getTitle(), is("list"));
    }

    @Test
    public void default_setup_yields_expected_values_on_link() {
        Mockito.when(app.getRouteBuilders(TestListServerResource.class)).thenReturn(routeBuilderList);
        Link link = LinkUtils.fromResource(app, TestListServerResource.class, "title");
        assertThat(link.getUri(), is(equalTo("/testapp/path")));
        assertThat(link.getNeedsAuthentication(), is(false));
        assertThat(link.getRefId(), is(nullValue()));
        assertThat(link.getRel(), is(equalTo(LinkRelation.COLLECTION)));
        assertThat(link.getRole(), is(equalTo(LinkRole.DEFAULT)));
        assertThat(link.getTitle(), is(equalTo("list")));
        assertThat(link.getImage(), is(nullValue()));
    }

    @Test
    public void attributes_are_replaced() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("id", "id");
        attributes.put("value", "value");
        String replaced = LinkUtils.replaceValues("/abc/{id}/{value}/xyz", attributes);
        assertThat(replaced, is("/abc/id/value/xyz"));
    }

    @Test
    public void testName() {
        LinkUtils linkUtils = new LinkUtils();
        TestListServerResource resource = new TestListServerResource();
        Object entity = Mockito.mock(Identifiable.class);
        Class<? extends SkysailServerResource<?>>[] classes = null;
        List<Link> fromResources = linkUtils.fromResources(resource, entity, classes );
        System.out.println(fromResources);
    }

//
//    @Test
//    public void no_classes_yield_no_links() {
//        Mockito.when(app.getRouteBuilders(TestListServerResource.class)).thenReturn(routeBuilderList);
//        SkysailServerResource<?> skysailServerResource = new TestListServerResource();
//        Object entity = "stringEntity";
//        Class<? extends SkysailServerResource<?>>[] classes = new Class[0];
//        List<Link> links = LinkUtils.fromResources(skysailServerResource, entity, classes);
//        assertThat(links.size(), is(0));
//    }

//    @Test
//    @Ignore
//    public void testName() {
//        Mockito.when(app.getRouteBuilders(TestListServerResource.class)).thenReturn(routeBuilderList);
//        SkysailServerResource<?> skysailServerResource = new TestListServerResource();
//        Object entity = "stringEntity";
//        Class<? extends SkysailServerResource<?>>[] classes = new Class[1];
//        classes[0] = TestListServerResource.class;
//        List<Link> links = LinkUtils.fromResources(skysailServerResource, entity, classes);
//     //   LinkUtils.fromResources(null, null, new Class[0]);//TestListServerResource.class);//;  (app, TestListServerResource.class, "title");
//        assertThat(links.size(), is(1));
//        //        assertThat(link.getUri(), is(equalTo("/testapp/path")));
////        assertThat(link.getNeedsAuthentication(), is(false));
////        assertThat(link.getRefId(), is(nullValue()));
////        assertThat(link.getRel(), is(equalTo(LinkRelation.COLLECTION)));
////        assertThat(link.getRole(), is(equalTo(LinkRole.DEFAULT)));
////        assertThat(link.getTitle(), is(equalTo("list")));
////        assertThat(link.getImage(), is(nullValue()));
//    }

}
