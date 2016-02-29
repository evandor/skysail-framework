package de.twenty11.skysail.server.mgt.events;

import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.*;

import org.osgi.service.event.Event;

public class EventsResource extends ListServerResource<EventDescriptor> {

    public EventsResource() {
        super(null);
        addToContext(ResourceContextId.LINK_TITLE, "Events");
    }

    @Override
    public List<EventDescriptor> getEntity() {
        List<EventDescriptor> result = new ArrayList<EventDescriptor>();
        List<Event> events = EventServiceProvider.getEvents();
        for (Event event : events) {
            result.add(new EventDescriptor(event));
        }
        return result;
    }
//    @Override
//    public List<Link> getLinks() {
//        return super.getLinks(LogResource.class, PeersResource.class, HeapStatsResource.class,
//                ServerLoadResource.class, ServerTimeResource.class, ApplicationsResource.class);
//    }

}
