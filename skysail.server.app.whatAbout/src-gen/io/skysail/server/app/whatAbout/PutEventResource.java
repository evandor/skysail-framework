package io.skysail.server.app.whatAbout;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import java.util.Date;
import org.restlet.resource.ResourceException;

public class PutEventResource extends PutEntityServerResource<io.skysail.server.app.whatAbout.Event> {


    private String id;
    private WhatAboutApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (WhatAboutApplication)getApplication();
    }

    @Override
    public void updateEntity(Event  entity) {
        io.skysail.server.app.whatAbout.Event original = getEntity();
        copyProperties(original,entity);

        app.getRepository(io.skysail.server.app.whatAbout.Event.class).update(original,app.getApplicationModel());
    }

    @Override
    public io.skysail.server.app.whatAbout.Event getEntity() {
        return (io.skysail.server.app.whatAbout.Event)app.getRepository(io.skysail.server.app.whatAbout.Event.class).findOne(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(EventsResource.class);
    }
}