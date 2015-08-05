package de.twenty11.skysail.server.mgt.events;

import org.osgi.service.event.Event;

public class EventDescriptor {

    private Event backingEvent;

    public EventDescriptor(Event event) {
        this.backingEvent = event;
    }

    public String getTopic() {
        return backingEvent.getTopic();
    }

}
