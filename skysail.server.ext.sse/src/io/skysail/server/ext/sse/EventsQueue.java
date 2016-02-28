package io.skysail.server.ext.sse;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import org.osgi.service.event.Event;

import io.skysail.server.EventHelper;

@Slf4j
public class EventsQueue {
    
    private Map<String, LinkedBlockingQueue<Message>> userMessages = new ConcurrentHashMap<>();

    private LinkedBlockingQueue<Message> publicMessages = new LinkedBlockingQueue<>(100);
    
    public synchronized void add(Event event) {
        String username = (String)event.getProperty(EventHelper.EVENT_USERNAME);
       
        if (username == null || username.trim().equals("")) {
            Message publicMessage = new Message(event);
            publicMessages.add(publicMessage);
            log.info("added new message {} to public queue, size now is {}", publicMessage, publicMessages.size());
            return;
        }
        if (!userMessages.containsKey(username)) {
            LinkedBlockingQueue<Message> queue = new LinkedBlockingQueue<>(100);
            userMessages.put(username, queue);
            log.info("adding new user {} to userMessages queue", username);
        }
        try {
            Message message = new Message(event);
            userMessages.get(username).put(message);
            log.info("added new message to userMessages queue, size now is {}", userMessages.get(username).size());
        } catch (InterruptedException e) {
            log.error(e.getMessage(),e);
        }
    }

    public List<Message> getFor(String username) {
        List<Message> result = new LinkedList<>();
        long expiresThreshold = new Date().getTime();
        publicMessages.stream().filter(pm -> notExpiredYet(pm, expiresThreshold)).forEach(m -> {
            result.add(m);
        });
        
        publicMessages.clear();
        
        if (username == null) {
            return result;
        }
        
        LinkedBlockingQueue<Message> queue = userMessages.get(username);
        if (queue != null) {
            queue.drainTo(result);
        }
        return result.stream().filter(m -> notExpiredYet(m, expiresThreshold)).collect(Collectors.toList());
    }

    private boolean notExpiredYet(Message pm, long expiresThreshold) {
        if (pm.getExpires() == 0L) {
            return true;
        }
        if (expiresThreshold > pm.getExpires()) {
            //expiredMessages.add(pm);
            log.info("discarding expired message: " + pm);
            return false;
            
        }
        return true;
    }

}
