package io.skysail.server.ext.sse;

import java.util.List;

import org.osgi.service.component.annotations.*;
import org.osgi.service.event.*;

import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import io.skysail.server.EventHelper;
import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.ext.sse.resources.GuiMessageResource;
import lombok.Getter;

@Component(immediate = true, property = { "event.topics=" + EventHelper.GUI_MSG + "/*" })
public class SseApplication extends SkysailApplication implements ApplicationProvider, EventHandler {

    private static final String APP_NAME = "SSE";
    
    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    @Getter
    private volatile EventAdmin eventAdmin;


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
//        router.attach(new RouteBuilder("/test", TestSseResource.class));
        router.attach(new RouteBuilder("", GuiMessageResource.class));
    }

    public List<Message> getEvents(String username) {
        return events.getFor(username);
    }



}
