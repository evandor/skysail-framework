package io.skysail.server.restlet.filter.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.server.restlet.filter.ExtractStandardQueryParametersResourceFilter;
import io.skysail.server.restlet.resources.SkysailServerResource;

import java.util.concurrent.ConcurrentHashMap;

import org.junit.*;
import org.mockito.Mockito;
import org.restlet.*;

public class ExtractStandardQueryParametersResourceFilterTest {

    private ExtractStandardQueryParametersResourceFilter<SkysailServerResource<String>, String> filter;
    private SkysailServerResource<String> resource;
    private ConcurrentHashMap<String,Object> attributes;
    private Response response;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        filter = new ExtractStandardQueryParametersResourceFilter<SkysailServerResource<String>, String>();
        response = Mockito.mock(Response.class);
        resource = Mockito.mock(SkysailServerResource.class);
        Request request = Mockito.mock(Request.class);
        attributes = new ConcurrentHashMap<>();
        Mockito.when(request.getAttributes()).thenReturn(attributes);
        Mockito.when(resource.getRequest()).thenReturn(request);
    }
    
    @Test
    public void _page_query_parameter_is_added_to_attributes() {
        Mockito.when(resource.getQueryValue(SkysailServerResource.PAGE_PARAM_NAME)).thenReturn("2");
        filter.handle(resource, response);
        assertThat(attributes.get(SkysailServerResource.PAGE_PARAM_NAME),is(equalTo("2")));
    }

    @Test
    public void _filter_query_parameter_is_added_to_attributes() {
        Mockito.when(resource.getQueryValue(SkysailServerResource.FILTER_PARAM_NAME)).thenReturn("thefilter");
        filter.handle(resource, response);
        assertThat(attributes.get(SkysailServerResource.FILTER_PARAM_NAME),is(equalTo("thefilter")));
    }

}
