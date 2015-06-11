package io.skysail.server.ext.sse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import lombok.extern.slf4j.Slf4j;

import org.osgi.service.event.Event;

import de.twenty11.skysail.server.core.osgi.EventHelper;

@Slf4j
public class EventsQueue {
    
    private Map<String, LinkedBlockingQueue<Message>> messages = new ConcurrentHashMap<>();

    public synchronized void add(Event event) {
        String username = (String)event.getProperty(EventHelper.EVENT_USERNAME);
        if (username == null) {
            return;
        }
        if (!messages.containsKey(username)) {
            LinkedBlockingQueue<Message> queue = new LinkedBlockingQueue<>(100);
            messages.put(username, queue);
        }
        try {
            Message message = new Message(event);
            messages.get(username).put(message);
        } catch (InterruptedException e) {
            log.error(e.getMessage(),e);
        }
    }

    public List<Message> getFor(String username) {
        LinkedBlockingQueue<Message> queue = messages.get(username);
        if (queue == null) {
            return Collections.emptyList();
        }
        List<Message> result = new ArrayList<>();
        queue.drainTo(result);
        return result;
        //return new ArrayList<Message>(queue);
    }

}
