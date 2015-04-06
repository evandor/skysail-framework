package io.skysail.server.restlet;

import io.skysail.server.restlet.RequestHandler;
import io.skysail.server.restlet.filter.AbstractResourceFilter;
import io.skysail.server.restlet.resources.EntityServerResource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Method;

import de.twenty11.skysail.server.app.SkysailApplication;

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
        AbstractResourceFilter filter = requestHandler.createForEntity(Method.DELETE);
        Response result = filter.handle(entityServerResource, response).getResponse();
        // assertThat(result.getStatus(),is(Status.CLIENT_ERROR_BAD_REQUEST));
    }
}
