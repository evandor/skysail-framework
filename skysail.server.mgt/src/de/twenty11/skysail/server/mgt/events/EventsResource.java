package de.twenty11.skysail.server.mgt.events;

import io.skysail.server.restlet.resources.ListServerResource;

import java.util.*;

import org.osgi.service.event.Event;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import de.twenty11.skysail.server.mgt.apps.ApplicationsResource;
import de.twenty11.skysail.server.mgt.jmx.HeapStatsResource;
import de.twenty11.skysail.server.mgt.load.ServerLoadResource;
import de.twenty11.skysail.server.mgt.log.LogResource;
import de.twenty11.skysail.server.mgt.peers.PeersResource;
import de.twenty11.skysail.server.mgt.time.ServerTimeResource;

public class EventsResource extends ListServerResource<EventDescriptor> {

    public EventsResource() {
        super(null);
        addToContext(ResourceContextId.LINK_TITLE, "Events");
    }

    @Override
    public List<EventDescriptor> getData() {
        List<EventDescriptor> result = new ArrayList<EventDescriptor>();
        List<Event> events = EventServiceProvider.getEvents();
        for (Event event : events) {
            result.add(new EventDescriptor(event));
        }
        return result;
    }
    @Override
    public List<Linkheader> getLinkheader() {
        return super.getLinkheader(LogResource.class, PeersResource.class, HeapStatsResource.class,
                ServerLoadResource.class, ServerTimeResource.class, ApplicationsResource.class);
    }

}
