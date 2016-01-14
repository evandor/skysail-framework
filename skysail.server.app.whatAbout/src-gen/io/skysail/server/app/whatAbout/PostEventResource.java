package io.skysail.server.app.whatAbout;

import java.util.Date;

import io.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.resource.ResourceException;

public class PostEventResource extends PostEntityServerResource<io.skysail.server.app.whatAbout.Event> {

	private WhatAboutApplication app;

    public PostEventResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new ");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (WhatAboutApplication) getApplication();
    }

    @Override
    public io.skysail.server.app.whatAbout.Event createEntityTemplate() {
        return new Event();
    }

    @Override
    public void addEntity(io.skysail.server.app.whatAbout.Event entity) {
        Subject subject = SecurityUtils.getSubject();
        String id = app.getRepository(io.skysail.server.app.whatAbout.Event.class).save(entity, app.getApplicationModel()).toString();
        entity.setId(id);

    }

    @Override
    public String redirectTo() {
        return super.redirectTo(EventsResource.class);
    }
}