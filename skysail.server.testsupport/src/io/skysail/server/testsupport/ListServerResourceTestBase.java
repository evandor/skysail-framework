package io.skysail.server.testsupport;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.restlet.Request;
import org.restlet.Response;

public class ListServerResourceTestBase {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    protected Response response;
    protected Request request;

    protected ConcurrentMap<String, Object> concurrentMap;

    @Before
    public void setUp() throws Exception {
        request = Mockito.mock(Request.class);
        response = Mockito.mock(Response.class);

        Mockito.when(response.getRequest()).thenReturn(request);
        concurrentMap = new ConcurrentHashMap<>();
        Mockito.when(response.getAttributes()).thenReturn(concurrentMap);

    }
}
