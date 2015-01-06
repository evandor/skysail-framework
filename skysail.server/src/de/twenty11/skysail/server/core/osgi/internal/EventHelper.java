package de.twenty11.skysail.server.core.osgi.internal;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.restlet.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Some static utility methods to fire events.
 * 
 */
public class EventHelper {

	private static final Logger log = LoggerFactory
			.getLogger(EventHelper.class);

	public static final String EVENT_PROPERTY_METHOD = "method";

	public static final String EVENT_PROPERTY_ENTITY = "entity";

	public static final String EVENT_PROPERTY_PATH = "path";

	private volatile EventAdmin eventAdmin;

	public EventHelper(EventAdmin eventAdmin) {
		this.eventAdmin = eventAdmin;
	}

	public String fireEvent(Request request) {
		if (eventAdmin == null) {
			log.warn("eventAdmin is null, cannot fire Event");
			return null;
		}
		return fire("request", request,null);
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
		String origRequestPath = (request.getOriginalRef().getPath() + "/")
				.replace("//", "/");
		String topic = (maintopic + "/" + origRequestPath).replace("//", "/")
				+ request.getMethod().toString();
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
			log.warn(
					"Exception caught when trying to post event with topic '{}'",
					topic);
		}
	}

}
