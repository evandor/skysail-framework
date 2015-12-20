package de.twenty11.skysail.server.mgt.events;

import lombok.*;

import org.osgi.service.event.Event;

import io.skysail.domain.Identifiable;

@Getter
@Setter
public class EventDescriptor implements Identifiable {

    private String id;
    private Event backingEvent;

    public EventDescriptor(Event event) {
        this.backingEvent = event;
    }

    public String getTopic() {
        return backingEvent.getTopic();
    }

}
