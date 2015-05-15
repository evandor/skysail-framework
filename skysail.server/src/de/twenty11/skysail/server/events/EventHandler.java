package de.twenty11.skysail.server.events;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import org.osgi.service.event.*;

import aQute.bnd.annotation.component.*;

@Component
public class EventHandler {

    private static AtomicReference<EventAdmin> eventAdminRef = new AtomicReference<>();

    @Reference(dynamic = true, multiple = false, optional = true)
    public void setEventAdmin(EventAdmin eventAdmin) {
        EventHandler.eventAdminRef.set(eventAdmin);
    }

    public void unsetEventAdmin(EventAdmin eventAdmin) {
        EventHandler.eventAdminRef.compareAndSet(eventAdmin, null);
    }
    
    public static synchronized EventInvocationResult sendEvent(String topic, String msg, String type) {
        if (eventAdminRef.get() == null) {
            return EventInvocationResult.EVENT_ADMIN_NOT_AVAILABLE;
        }
        Map<String, String> map = new HashMap<>();
        map.put("msg", msg);
        map.put("type", type);
        eventAdminRef.get().postEvent(new Event(topic, map));
        return EventInvocationResult.SENT;
    }

}
