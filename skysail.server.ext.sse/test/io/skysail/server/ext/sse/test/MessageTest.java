package io.skysail.server.ext.sse.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.ext.sse.Message;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.testsupport.ResourceTestBase;

import java.util.HashMap;

import org.apache.shiro.subject.SimplePrincipalMap;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.osgi.service.event.Event;

import de.twenty11.skysail.server.core.osgi.EventHelper;

public class MessageTest extends ResourceTestBase {

    private Event event;

    @Before
    public void setUp() throws Exception {
        super.setUp(Mockito.mock(SkysailApplication.class), Mockito.mock(SkysailServerResource.class));
        Mockito.when(subjectUnderTest.getPrincipal()).thenReturn("admin");
        Mockito.when(subjectUnderTest.getPrincipals()).thenReturn(new SimplePrincipalMap(new HashMap<>()));
        setSubject(subjectUnderTest);
    }

    @Test
    @Ignore
    public void messages_for_same_event_are_equal() throws Exception {
        Event event = new EventHelper(null).channel("topic").info("msg").getEvent();
        Message m1 = new Message(event);
        Message m2 = new Message(event);
        assertThat(m1,is(equalTo(m2)));
    }
}
