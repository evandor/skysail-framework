package de.twenty11.skysail.server.core.restlet.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
        assertThat(routeBuilder.getPathTemplate(), is(equalTo("/path")));
        assertThat(routeBuilder.getRolesForAuthorization(), is(nullValue()));
    }

}
