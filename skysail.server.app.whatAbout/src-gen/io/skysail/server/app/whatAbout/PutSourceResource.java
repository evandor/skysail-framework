package io.skysail.server.app.whatAbout;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import java.util.Date;
import org.restlet.resource.ResourceException;

public class PutSourceResource extends PutEntityServerResource<io.skysail.server.app.whatAbout.Source> {


    private String id;
    private WhatAboutApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (WhatAboutApplication)getApplication();
    }

    @Override
    public void updateEntity(Source  entity) {
        io.skysail.server.app.whatAbout.Source original = getEntity();
        copyProperties(original,entity);

        app.getRepository(io.skysail.server.app.whatAbout.Source.class).update(original,app.getApplicationModel());
    }

    @Override
    public io.skysail.server.app.whatAbout.Source getEntity() {
        return (io.skysail.server.app.whatAbout.Source)app.getRepository(io.skysail.server.app.whatAbout.Source.class).findOne(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(SourcesResource.class);
    }
}