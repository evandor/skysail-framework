package io.skysail.server.test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;

import io.skysail.server.SkysailStatusService;

public class SkysailStatusServiceTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testName() throws IOException {
        Request request = Mockito.mock(Request.class);
        Response response = Mockito.mock(Response.class);
        Representation rep = new SkysailStatusService().toRepresentation(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED, request, response);
        assertThat(rep.getMediaType(),is(MediaType.TEXT_HTML));
        assertThat(rep.getText().toString(), containsString("there was a problem"));
    }
}
