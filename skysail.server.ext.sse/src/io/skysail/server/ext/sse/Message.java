package io.skysail.server.ext.sse;

import lombok.Getter;

import org.osgi.service.event.Event;

import de.twenty11.skysail.server.core.osgi.EventHelper;


@Getter
public class Message {

    private String topic;
    private Integer id;
    private String message;
    private String type;

    public Message(Event event) {
        topic = event.getTopic();
        id = (Integer)event.getProperty(EventHelper.EVENT_ID);
        message = (String)event.getProperty(EventHelper.EVENT_MESSAGE);
        type = (String)event.getProperty(EventHelper.EVENT_TYPE);
    }

    public Message(String message, String type) {
        this.message = message;
        this.type = type;
    }

}
