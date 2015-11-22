package de.twenty11.skysail.server.mgt.events;

import io.skysail.api.domain.Identifiable;
import lombok.*;

import org.osgi.service.event.Event;

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
