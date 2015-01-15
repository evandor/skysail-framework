package de.twenty11.skysail.server.core.restlet.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Method;

import de.twenty11.skysail.server.app.SkysailApplication;
import de.twenty11.skysail.server.core.restlet.EntityServerResource;
import de.twenty11.skysail.server.core.restlet.RequestHandler;
import de.twenty11.skysail.server.core.restlet.filter.AbstractResourceFilter;

public class RequestHandlerTest {

    private RequestHandler requestHandler;
    private EntityServerResource<String> entityServerResource;
    private Response response;

    @Before
    public void setUp() {
        SkysailApplication application = Mockito.mock(SkysailApplication.class);
        requestHandler = new RequestHandler(application);
        entityServerResource = Mockito.mock(EntityServerResource.class);
        Request request = Mockito.mock(Request.class);
        response = new Response(request);
    }

    @Test
    public void testName() {
        AbstractResourceFilter filter = requestHandler.createForEntity(Method.DELETE, null);
        Response result = filter.handle(entityServerResource, response).getResponse();
        // assertThat(result.getStatus(),is(Status.CLIENT_ERROR_BAD_REQUEST));
    }
}
