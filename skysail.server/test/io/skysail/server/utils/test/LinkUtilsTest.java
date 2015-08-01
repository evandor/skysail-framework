package io.skysail.server.utils.test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import io.skysail.api.links.*;
import io.skysail.server.app.SkysailApplication;
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
        routeBuilderList = new ArrayList<>();
        RouteBuilder routeBuilder = Mockito.mock(RouteBuilder.class);
        Mockito.when(routeBuilder.getPathTemplate()).thenReturn("/path");
        routeBuilderList.add(routeBuilder);
    }

    @Test
    public void unknown_routeBuilder_yields_null_link() {
        Link link = LinkUtils.fromResource(app, TestListServerResources.class, "title");
        assertThat(link, is(nullValue()));
    }

    @Test
    public void default_setup_yields_expected_values_on_link() {
        Mockito.when(app.getRouteBuilders(TestListServerResources.class)).thenReturn(routeBuilderList);
        Link link = LinkUtils.fromResource(app, TestListServerResources.class, "title");
        assertThat(link.getUri(), is(equalTo("/testapp/path")));
        assertThat(link.getNeedsAuthentication(),is(false));
        assertThat(link.getRefId(),is(nullValue()));
        assertThat(link.getRel(),is(equalTo(LinkRelation.COLLECTION)));
        assertThat(link.getRole(),is(equalTo(LinkRole.DEFAULT)));
        assertThat(link.getTitle(),is(equalTo("list")));
        assertThat(link.getImage(), is(nullValue()));
    }

}
