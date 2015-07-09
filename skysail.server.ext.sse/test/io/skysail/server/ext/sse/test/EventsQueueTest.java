package io.skysail.server.ext.sse.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.ext.sse.EventsQueue;
import io.skysail.server.ext.sse.Message;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.testsupport.ResourceTestBase;

import java.util.HashMap;
import java.util.List;

import org.apache.shiro.subject.SimplePrincipalMap;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.osgi.service.event.Event;

import de.twenty11.skysail.server.core.osgi.EventHelper;

public class EventsQueueTest  extends ResourceTestBase {

    private Event event;

    @Before
    public void setUp() throws Exception {
        super.setUp(Mockito.mock(SkysailApplication.class), Mockito.mock(SkysailServerResource.class));
        Mockito.when(subjectUnderTest.getPrincipals()).thenReturn(new SimplePrincipalMap(new HashMap<>()));
        setSubject(subjectUnderTest);
        event = new EventHelper(null).channel("topic").info("msg").getEvent();
    }

    @Test
    public void testName() throws Exception {
        Mockito.when(subjectUnderTest.getPrincipal()).thenReturn(null);
        EventsQueue eventsQueue = new EventsQueue();
        eventsQueue.add(event);
        List<Message> messages = eventsQueue.getFor(null);
        assertThat(messages.size(),is(1));
        assertThat(messages.get(0).getMessage(), is(equalTo("msg")));
    }
}