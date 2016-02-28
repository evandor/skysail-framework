package de.twenty11.skysail.server.core.osgi;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.restlet.Request;
import org.restlet.data.Method;
import org.restlet.data.Reference;

import io.skysail.server.EventHelper;

public class EventHelperTest {

    @Mock
    private EventAdmin eventAdmin;

    @Mock
    private Request request;

    private EventHelper eventHelper;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        eventHelper = new EventHelper(eventAdmin);
    }

    @Test
    public void returns_if_eventAdmin_is_null() {
        EventHelper myEventHelper = new EventHelper(null);
        myEventHelper.fireEvent(request);
        verify(eventAdmin, times(0)).postEvent(Mockito.any(Event.class));
        verify(eventAdmin, times(0)).sendEvent(Mockito.any(Event.class));
    }

    @Test
    public void handles_simple_request() {
        setUpForRequest("hi");

        String topic = eventHelper.fireEvent(request);

        verify(eventAdmin, times(1)).postEvent(Mockito.any(Event.class));
        assertThat(topic, is(equalTo("request/hi/GET")));
    }

    @Test
    public void handles_empty_request_path() {
        setUpForRequest("");

        String topic = eventHelper.fireEvent(request);

        verify(eventAdmin, times(1)).postEvent(Mockito.any(Event.class));
        assertThat(topic, is(equalTo("request/GET")));
    }

    @Test
    public void handles_root_request_path() {
        setUpForRequest("/");

        String topic = eventHelper.fireEvent(request);

        verify(eventAdmin, times(1)).postEvent(Mockito.any(Event.class));
        assertThat(topic, is(equalTo("request/GET")));
    }

    private void setUpForRequest(String path) {
        Reference origRef = mock(Reference.class);
        when(request.getOriginalRef()).thenReturn(origRef);
        when(request.getMethod()).thenReturn(Method.GET);
        when(origRef.getPath()).thenReturn(path);
    }
}
