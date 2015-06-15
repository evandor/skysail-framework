package de.twenty11.skysail.server.core.osgi;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;

import org.apache.shiro.SecurityUtils;
import org.osgi.service.event.*;
import org.restlet.Request;

/**
 * Some utility methods to fire events.
 * 
 */
@Slf4j
public class EventHelper {

    public static final String EVENT_ID = "msgId";
    public static final String EVENT_TYPE = "type";
    public static final String EVENT_TIME = "time";
    public static final String EVENT_USERNAME = "username";
    public static final String EVENT_MESSAGE = "msg";

    public static final String EVENT_PROPERTY_METHOD = "method";
    public static final String EVENT_PROPERTY_ENTITY = "entity";
    public static final String EVENT_PROPERTY_PATH = "path";

    public static final String GUI_MSG = "GUI/message";

    private volatile EventAdmin eventAdmin;

    private static AtomicInteger msgIdCounter = new AtomicInteger(1);

    private String topic;

    private String msg;

    private String type;
    private int obsoleteEventId;

    public EventHelper(EventAdmin eventAdmin) {
        this.eventAdmin = eventAdmin;
    }

    public String fireEvent(Request request) {
        if (eventAdmin == null) {
            log.warn("eventAdmin is null, cannot fire Event");
            return null;
        }
        return fire("request", request, null);
    }

    public String fireEvent(String topic, Request request, Object entity) {
        if (eventAdmin == null) {
            log.warn("eventAdmin is null, cannot fire Event");
            return null;
        }
        return fire(topic, request, entity);
    }

    @SuppressWarnings("unchecked")
    private String fire(String maintopic, Request request, Object entity) {
        String origRequestPath = (request.getOriginalRef().getPath() + "/").replace("//", "/");
        String topic = (maintopic + "/" + origRequestPath).replace("//", "/") + request.getMethod().toString();
        @SuppressWarnings("rawtypes")
        Dictionary properties = new Hashtable();
        properties.put(EVENT_PROPERTY_PATH, origRequestPath);
        if (entity != null) {
            properties.put(EVENT_PROPERTY_ENTITY, entity);
        }
        postEvent(topic, properties);
        return topic;
    }

    @SuppressWarnings("rawtypes")
    private void postEvent(String topic, Dictionary properties) {
        try {
            Event newEvent = new Event(topic, properties);
            eventAdmin.postEvent(newEvent);
        } catch (Exception e) {
            log.warn("Exception caught when trying to post event with topic '{}'", topic);
        }
    }

    public EventHelper channel(String mainTopic) {
        this.topic = mainTopic;
        return this;
    }

    public EventHelper info(String msg) {
        this.msg = msg;
        this.topic += "/info";
        this.type = "success";
        return this;
    }

    public EventHelper error(String msg) {
        this.msg = msg;
        this.topic += "/error";
        this.type = "error";
        return this;
    }
    
    public EventHelper markObsolete(int msgId) {
        this.obsoleteEventId = msgId;
        this.msg = "marking msg obsolete";
        this.topic += "/obsolete";
        this.type = "obsolete";
        return this;
    }

    public int fire() {
        if (eventAdmin == null) {
            log.warn("eventAdmin is null, cannot fire Event");
            return -1;
        }
        Dictionary<String, Object> properties = new Hashtable<>();
        properties.put(EVENT_MESSAGE, msg);
        Object principal = SecurityUtils.getSubject().getPrincipal();
        properties.put(EVENT_USERNAME, principal != null ? principal.toString() : "");
        properties.put(EVENT_TIME, System.currentTimeMillis());
        properties.put(EVENT_TYPE, type);
        int msgId = msgIdCounter.incrementAndGet();
        properties.put(EVENT_ID, msgId);

        Event event = new Event(topic, properties);
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                eventAdmin.postEvent(event);
            };
        }.start();
        
        return msgId;
    }

   

}
