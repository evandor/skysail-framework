package io.skysail.server;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.shiro.SecurityUtils;
import org.osgi.service.event.*;
import org.restlet.Request;

import lombok.extern.slf4j.Slf4j;

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
    public static final String EVENT_EXPIRES = "expires";
    public static final String EVENT_MESSAGE = "msg";

    public static final String EVENT_PROPERTY_METHOD = "method";
    public static final String EVENT_PROPERTY_ENTITY = "entity";
    public static final String EVENT_PROPERTY_PATH = "path";

    public static final String GUI = "GUI";
    public static final String GUI_MSG = "GUI/message";
    public static final String GUI_PEITY_BAR = "GUI/peitybar";

    private volatile EventAdmin eventAdmin;

    private static final AtomicInteger msgIdCounter = new AtomicInteger(1);

    private String topic;
    private String msg;
    private String type;
    private long expires;

    public EventHelper(EventAdmin eventAdmin) {
        this.eventAdmin = eventAdmin;
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

    public EventHelper lifetime(long ms) {
        expires = new Date().getTime() + ms;
        //log.info("setting lifetime to " + expires);
        return this;
    }

    public Event getEvent() {
        return createEvent();
    }

    public String fireEvent(Request request) {
        if (eventAdmin == null) {
            log.warn("eventAdmin is null, cannot fire Event");
            return null;
        }
        return fire("request", request, null);
    }

    public synchronized int fire() {
        if (eventAdmin == null) {
            log.warn("eventAdmin is null, cannot fire Event");
            return -1;
        }
        Event event = createEvent();
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                }
                log.debug("about to post event "  + event + ": " + event.getProperty(EVENT_MESSAGE));
                eventAdmin.postEvent(event);
            };
        }.start();

        return msgIdCounter.get();
    }

    private Event createEvent() {
        msgIdCounter.incrementAndGet();
        return new Event(topic, createEventProperties());
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
            log.debug("Exception caught when trying to post event with topic '{}'", topic);
        }
    }

    private Dictionary<String, Object> createEventProperties() {
        Object principal = SecurityUtils.getSubject().getPrincipal();

        Dictionary<String, Object> properties = new Hashtable<>();
        properties.put(EVENT_MESSAGE, msg);
        properties.put(EVENT_USERNAME, principal != null ? principal.toString() : "");
        properties.put(EVENT_TIME, System.currentTimeMillis());
        properties.put(EVENT_TYPE, type);
        if (expires > 0) {
            properties.put(EVENT_EXPIRES, expires);
        }
        properties.put(EVENT_ID, msgIdCounter.get());
        return properties;
    }



}
