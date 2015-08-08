package de.twenty11.skysail.server.core.restlet.test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.server.app.ApiVersion;

import org.junit.*;

import de.twenty11.skysail.server.core.restlet.RouteBuilder;

public class RouteBuilderTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testRouteBuilderStringRestlet() throws Exception {
        RouteBuilder routeBuilder = new RouteBuilder("/path", TestServerResource.class);
        assertThat(routeBuilder.getPathTemplate(new ApiVersion(1)), is(equalTo("/v1/path")));
        assertThat(routeBuilder.getRolesForAuthorization(), is(nullValue()));
    }

}
