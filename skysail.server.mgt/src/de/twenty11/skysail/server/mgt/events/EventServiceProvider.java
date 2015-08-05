package de.twenty11.skysail.server.mgt.events;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.Buffer;
import org.apache.commons.collections.BufferUtils;
import org.apache.commons.collections.buffer.BoundedFifoBuffer;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

//@Component(immediate = true, properties = { "event.topics=*" })
/**
 * 
 * Seems like activating this (and listening to all topics?) is not a good idea in terms of CPU consumption...
 * 
 */

public class EventServiceProvider implements EventHandler {

    private static final int MAX_SIZE = 100;

    private static final Buffer eventBuffer = BufferUtils.synchronizedBuffer(new BoundedFifoBuffer(MAX_SIZE));

    @SuppressWarnings("unchecked")
    @Override
    public void handleEvent(Event event) {
        eventBuffer.add(event);
    }

    public static List<Event> getEvents() {
        @SuppressWarnings("unchecked")
        Event[] array = (Event[]) eventBuffer.toArray(new Event[eventBuffer.size()]);
        return Arrays.asList(array);
    }
}
