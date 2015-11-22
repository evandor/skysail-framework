package de.twenty11.skysail.server.mgt.captures;

import io.skysail.server.restlet.resources.ListServerResource;

import java.util.*;

import org.restlet.resource.ResourceException;

public class RequestCaptureResource extends ListServerResource<RequestCapture> {

    private Map<String, Integer> requestcapture;

    public RequestCaptureResource() {
        super(null);
    }

    @Override
    protected void doInit() throws ResourceException {
        requestcapture = null;//RequestCapturer.getRequestcapture();
    }

    @Override
    public List<RequestCapture> getEntity() {
        if (requestcapture == null) {
            return Collections.emptyList();
        }
        List<RequestCapture> captures = new ArrayList<RequestCapture>();
        for (String key : requestcapture.keySet()) {
            captures.add(new RequestCapture(key, requestcapture.get(key)));
        }
        return captures;
    }

}
