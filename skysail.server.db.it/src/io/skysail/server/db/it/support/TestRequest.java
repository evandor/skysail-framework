package io.skysail.server.db.it.support;

import java.util.concurrent.*;

import org.restlet.Request;
import org.restlet.data.Reference;

public class TestRequest extends Request {

    private ConcurrentMap<String, Object> attributes = new ConcurrentHashMap<>();

    @Override
    public Reference getResourceRef() {
        return new Reference();
    }

    @Override
    public ConcurrentMap<String, Object> getAttributes() {
        return attributes;
    }

    public synchronized void addAttribute(String key, String value) {
        attributes.put(key, value);
    }

    public void clearAttributes() {
        attributes = new ConcurrentHashMap<>();
    }
}
