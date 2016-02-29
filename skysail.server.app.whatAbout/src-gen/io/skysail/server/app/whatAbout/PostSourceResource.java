package io.skysail.server.app.whatAbout;

import java.util.Date;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.resource.ResourceException;

public class PostSourceResource extends PostEntityServerResource<io.skysail.server.app.whatAbout.Source> {

	private WhatAboutApplication app;

    public PostSourceResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new ");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (WhatAboutApplication) getApplication();
    }

    @Override
    public io.skysail.server.app.whatAbout.Source createEntityTemplate() {
        return new Source();
    }

    @Override
    public void addEntity(io.skysail.server.app.whatAbout.Source entity) {
        Subject subject = SecurityUtils.getSubject();
        String id = app.getRepository(io.skysail.server.app.whatAbout.Source.class).save(entity, app.getApplicationModel()).toString();
        entity.setId(id);

    }

    @Override
    public String redirectTo() {
        return super.redirectTo(SourcesResource.class);
    }
}