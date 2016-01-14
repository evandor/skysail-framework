package io.skysail.server.app.whatAbout;

import io.skysail.server.queryfilter.Filter;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.api.links.Link;

import java.util.List;
import java.util.Map;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class EventsResource extends ListServerResource<io.skysail.server.app.whatAbout.Event> {

    private WhatAboutApplication app;
    private EventRepository repository;

    public EventsResource() {
        super(EventResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list Events");
    }

    @Override
    protected void doInit() {
        app = (WhatAboutApplication) getApplication();
        repository = (EventRepository) app.getRepository(io.skysail.server.app.whatAbout.Event.class);
    }

    @Override
    public List<io.skysail.server.app.whatAbout.Event> getEntity() {
       return repository.find(new Filter(getRequest()));
    }

    public List<Link> getLinks() {
              return super.getLinks(PostEventResource.class,EventsResource.class,SourcesResource.class);
    }
}