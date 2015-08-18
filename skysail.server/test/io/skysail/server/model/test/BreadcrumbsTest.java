package io.skysail.server.model.test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.model.*;
import io.skysail.server.restlet.resources.SkysailServerResource;

import java.util.*;

import org.junit.*;
import org.mockito.Mockito;
import org.restlet.Request;
import org.restlet.data.Reference;
import org.restlet.routing.Route;
import org.restlet.util.RouteList;

public class BreadcrumbsTest {

    private SkysailServerResource<?> resource;
    private Reference reference;
    private Request request;

    private RouteList routes;

    @Before
    public void setUp() throws Exception {
        routes = new RouteList();
        resource = Mockito.mock(SkysailServerResource.class);
        reference = Mockito.mock(Reference.class);
        request = Mockito.mock(Request.class);
        Mockito.when(resource.getReference()).thenReturn(reference);
        Mockito.when(resource.getRequest()).thenReturn(request);
        Mockito.when(resource.getApplication()).thenReturn(new SkysailApplication("appName") {

            @Override
            public RouteList getRoutes() {
                return routes;
            }
        });
    }

    @Test
    public void creates_homelink_only_for_trivial_resource() throws Exception {
        List<Breadcrumb> bcs = new Breadcrumbs(null).create(resource);
        assertThat(bcs.size(),is(1));
        assertThat(bcs.get(0).getHref(),is("/"));
        assertThat(bcs.get(0).getValue(),is("<span class='glyphicon glyphicon-home'></span>"));
    }

    @Test
    public void creates_homelink_and_app_with_version_for_simple_resource() throws Exception {
        List<String> values = Arrays.asList("app","v2");
        Mockito.when(reference.getSegments()).thenReturn(values);
        List<Breadcrumb> bcs = new Breadcrumbs(null).create(resource);
        assertThat(bcs.size(),is(2));
        assertThat(bcs.get(1).getHref(),is("/appName/v1"));
        assertThat(bcs.get(1).getValue(),is(" appName (v1)"));
    }

    @Test
    @Ignore
    public void creates_homelink_and_app_with_version_for_list_resource() throws Exception {
        List<String> values = Arrays.asList("app","v2", "List");
        Route route = Mockito.mock(Route.class);
        routes.add(route);
        Mockito.when(reference.getSegments()).thenReturn(values);
        List<Breadcrumb> bcs = new Breadcrumbs(null).create(resource);
        assertThat(bcs.size(),is(3));
        assertThat(bcs.get(1).getHref(),is("/appName/v1"));
        assertThat(bcs.get(1).getValue(),is(" appName (v1)"));
    }
}
