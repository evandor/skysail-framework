package io.skysail.server.app.whatAbout;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class SourceResource extends EntityServerResource<io.skysail.server.app.whatAbout.Source> {

    private String id;
    private WhatAboutApplication app;
    private SourceRepository repository;

    public SourceResource() {
        addToContext(ResourceContextId.LINK_TITLE, "details");
        addToContext(ResourceContextId.LINK_GLYPH, "search");
    }

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (WhatAboutApplication) getApplication();
        repository = (SourceRepository) app.getRepository(io.skysail.server.app.whatAbout.Source.class);
    }


    @Override
    public SkysailResponse<?> eraseEntity() {
        repository.delete(id);
        return new SkysailResponse<>();
    }

    @Override
    public io.skysail.server.app.whatAbout.Source getEntity() {
        return (io.skysail.server.app.whatAbout.Source)app.getRepository().findOne(id);
    }

	@Override
    public List<Link> getLinks() {
        return super.getLinks(PutSourceResource.class);
    }

}