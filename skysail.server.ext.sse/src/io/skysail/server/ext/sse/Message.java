package io.skysail.server.ext.sse;

import lombok.Getter;
import lombok.ToString;

import org.osgi.service.event.Event;

import io.skysail.server.EventHelper;

@Getter
@ToString
public class Message {

    private String topic;
    private Integer id;
    private String message;
    private String type;
    private long expires;

    public Message(Event event) {
        topic = event.getTopic();
        id = (Integer) event.getProperty(EventHelper.EVENT_ID);
        message = (String) event.getProperty(EventHelper.EVENT_MESSAGE);
        type = (String) event.getProperty(EventHelper.EVENT_TYPE);
        setExpiresTimestamp(event);
    }

    public Message(String message, String type) {
        this.message = message;
        this.type = type;
    }

    private void setExpiresTimestamp(Event event) {
        Object expiresObject = event.getProperty(EventHelper.EVENT_EXPIRES);
        if (expiresObject != null) {
            expires = (long) expires;
        }
    }
    
}
