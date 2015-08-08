package de.twenty11.skysail.server.core.restlet.test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.server.app.ApiVersion;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.restlet.Context;

import de.twenty11.skysail.server.core.restlet.*;

@RunWith(MockitoJUnitRunner.class)
public class SkysailRouterTest {

	@InjectMocks
	private SkysailRouter skysailRouter;

	@Mock
	private Context context;

	@Test
    public void can_retrieve_attached_routeBuilder_by_its_pathname() throws Exception {
	    RouteBuilder routeBuilder = new RouteBuilder("/path", TestServerResource.class);
		skysailRouter.attach(routeBuilder);

	    assertThat(skysailRouter.getRouteBuilder("/path").getPathTemplate(new ApiVersion(1)), is("/v1/path"));
	    assertThat(skysailRouter.getRouteBuilders().size(), is(1));
	    assertThat(skysailRouter.getRouteBuildersForResource(TestServerResource.class).get(0),is(routeBuilder));
	    assertThat(skysailRouter.getTemplatePathForResource(TestServerResource.class).get(0), is("/path"));
    }
}
