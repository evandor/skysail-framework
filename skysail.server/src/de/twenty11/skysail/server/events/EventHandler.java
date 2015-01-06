package de.twenty11.skysail.server.events;

import java.util.HashMap;
import java.util.Map;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

@Component
public class EventHandler {

    private static EventAdmin eventAdmin;

    @Reference(dynamic = true, multiple = false, optional = true)
    public void setEventAdmin(EventAdmin eventAdmin) {
        EventHandler.eventAdmin = eventAdmin;
    }

    public void unsetEventAdmin(@SuppressWarnings("unused") EventAdmin eventAdmin) {
        EventHandler.eventAdmin = null;
    }
    
    public static synchronized EventInvocationResult sendEvent(String topic, String msg, String type) {
        if (eventAdmin == null) {
            return EventInvocationResult.EVENT_ADMIN_NOT_AVAILABLE;
        }
        Map<String, String> map = new HashMap<>();
        map.put("msg", msg);
        map.put("type", type);
        eventAdmin.postEvent(new Event(topic, map));
        return EventInvocationResult.SENT;
    }

}
