package io.skysail.server.ext.sse;

import io.skysail.server.app.SkysailApplication;
import io.skysail.server.ext.sse.resources.*;

import java.util.List;

import org.osgi.service.event.*;

import aQute.bnd.annotation.component.Component;
import de.twenty11.skysail.server.app.ApplicationProvider;
import de.twenty11.skysail.server.core.osgi.EventHelper;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;

@Component(immediate = true, properties = { "event.topics=" + EventHelper.GUI_MSG + "/*" })
public class SseApplication extends SkysailApplication implements ApplicationProvider, EventHandler {

    private static final String APP_NAME = "SSE";
    
    private EventsQueue events = new EventsQueue();
    
    public SseApplication() {
        super(APP_NAME);
    }

    @Override
    public void handleEvent(Event event) {
       events.add(event);
    }
    
    @Override
    protected void attach() {
        super.attach();
        router.attach(new RouteBuilder("/test", TestSseResource.class));
        router.attach(new RouteBuilder("", GuiMessageResource.class));
    }

    public List<Message> getEvents(String username) {
        return events.getFor(username);
    }
    
   

}
