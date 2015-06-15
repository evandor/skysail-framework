package io.skysail.server.ext.sse;

import java.util.*;
import java.util.concurrent.*;

import lombok.extern.slf4j.Slf4j;

import org.osgi.service.event.Event;

import de.twenty11.skysail.server.core.osgi.EventHelper;

@Slf4j
public class EventsQueue {
    
    private Map<String, LinkedBlockingQueue<Message>> userMessages = new ConcurrentHashMap<>();

    private LinkedBlockingQueue<Message> publicMessages = new LinkedBlockingQueue<>(100);
    
    public synchronized void add(Event event) {
        String username = (String)event.getProperty(EventHelper.EVENT_USERNAME);
        if (username == null || username.trim().equals("")) {
            publicMessages.add(new Message(event));
            return;
        }
        if (!userMessages.containsKey(username)) {
            LinkedBlockingQueue<Message> queue = new LinkedBlockingQueue<>(100);
            userMessages.put(username, queue);
        }
        try {
            Message message = new Message(event);
            userMessages.get(username).put(message);
        } catch (InterruptedException e) {
            log.error(e.getMessage(),e);
        }
    }

    public List<Message> getFor(String username) {
        List<Message> result = new LinkedList<>();
        publicMessages.stream().forEach(m -> {
            result.add(m);
        });
        
        publicMessages.clear();
        
        LinkedBlockingQueue<Message> queue = userMessages.get(username);
        if (queue != null) {
            queue.drainTo(result);
        }
        return result;
    }

}
