package io.skysail.server.app.whatAbout;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class EventResource extends EntityServerResource<io.skysail.server.app.whatAbout.Event> {

    private String id;
    private WhatAboutApplication app;
    private EventRepository repository;

    public EventResource() {
        addToContext(ResourceContextId.LINK_TITLE, "details");
        addToContext(ResourceContextId.LINK_GLYPH, "search");
    }

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (WhatAboutApplication) getApplication();
        repository = (EventRepository) app.getRepository(io.skysail.server.app.whatAbout.Event.class);
    }


    @Override
    public SkysailResponse<?> eraseEntity() {
        repository.delete(id);
        return new SkysailResponse<>();
    }

    @Override
    public io.skysail.server.app.whatAbout.Event getEntity() {
        return (io.skysail.server.app.whatAbout.Event)app.getRepository().findOne(id);
    }

	@Override
    public List<Link> getLinks() {
        return super.getLinks(PutEventResource.class);
    }

}