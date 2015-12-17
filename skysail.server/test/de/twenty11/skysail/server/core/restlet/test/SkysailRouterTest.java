package de.twenty11.skysail.server.core.restlet.test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.server.app.*;
import io.skysail.domain.core.ApplicationModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.restlet.*;
import org.restlet.resource.*;
import org.restlet.routing.Filter;

import de.twenty11.skysail.server.core.restlet.*;

@RunWith(MockitoJUnitRunner.class)
public class SkysailRouterTest {

	public class AFinder extends Finder {
	    @Override
	    public Class<? extends ServerResource> getTargetClass() {
	        return TestServerResource.class;
	    }
    }

    public class AFilter extends Filter {
	    @Override
	    public Restlet getNext() {
	        return new AFinder();
	    }
    }

    @InjectMocks
	private SkysailRouter skysailRouter;

	@Mock
	private Context context;

	@Mock
	private SkysailApplication skysailApplication;

	@Test
    public void can_retrieve_attached_routeBuilder_by_its_pathname() throws Exception {
	    Mockito.when(skysailApplication.getApplicationModel()).thenReturn(new ApplicationModel("id"));
	    RouteBuilder routeBuilder = new RouteBuilder("/path", TestServerResource.class);
		skysailRouter.attach(routeBuilder);

	    assertThat(skysailRouter.getRouteBuilder("/path").getPathTemplate(new ApiVersion(1)), is("/v1/path"));
	    assertThat(skysailRouter.getRouteBuilders().size(), is(1));
	    assertThat(skysailRouter.getRouteBuildersForResource(TestServerResource.class).get(0),is(routeBuilder));
	    assertThat(skysailRouter.getTemplatePathForResource(TestServerResource.class).get(0), is("/path"));
    }

	@Test
    public void testName() {
	    Filter myFilter = new AFilter();
        RouteBuilder routeBuilder = new RouteBuilder("/path", myFilter);
        skysailRouter.attach(routeBuilder);

        assertThat(skysailRouter.getRouteBuildersForResource(TestServerResource.class).get(0),is(routeBuilder));

    }
}
