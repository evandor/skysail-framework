package de.twenty11.skysail.server.events;

import java.util.concurrent.ConcurrentHashMap;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import de.twenty11.skysail.server.core.osgi.EventHelper;

/**
 * captures requests(method &amp; path) in a map, incrementing the count.
 * 
 */
// @Component(immediate = true, properties = { "event.topics=request/*" })
public class RequestCapturer implements EventHandler {

    private static final ConcurrentHashMap<String, Integer> requestCapture = new ConcurrentHashMap<String, Integer>();

    @Override
    public void handleEvent(Event event) {
        String path = (String) event.getProperty(EventHelper.EVENT_PROPERTY_PATH);
        String method = (String) event.getProperty(EventHelper.EVENT_PROPERTY_METHOD);
        String identifier = method + " " + path;
        Integer counter = requestCapture.get(identifier) == null ? 1 : 1 + requestCapture.get(identifier);
        requestCapture.put(identifier, counter);
    }

    public static ConcurrentHashMap<String, Integer> getRequestcapture() {
        return requestCapture;
    }

}
