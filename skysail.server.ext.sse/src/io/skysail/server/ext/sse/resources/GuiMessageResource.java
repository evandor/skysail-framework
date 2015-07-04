package io.skysail.server.ext.sse.resources;

import io.skysail.server.ext.sse.SseApplication;
import io.skysail.server.ext.sse.representation.SseWriterRepresentation;

import org.apache.shiro.SecurityUtils;
import org.restlet.representation.Representation;
import org.restlet.resource.*;

public class GuiMessageResource extends ServerResource {

    private SseApplication app;

    @Override
    protected void doInit() {
        app = (SseApplication) getApplication();
    }

    @Override
    protected Representation get() throws ResourceException {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        return new SseWriterRepresentation(username, app);
    }

}
