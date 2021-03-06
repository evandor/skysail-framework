package io.skysail.server.app.whatAbout;

import io.skysail.server.ResourceContextId;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.api.links.Link;

import java.util.*;

public class SourcesResource extends ListServerResource<io.skysail.server.app.whatAbout.Source> {

    private WhatAboutApplication app;
    private SourceRepository repository;

    public SourcesResource() {
        super(SourceResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list Sources");
    }

    @Override
    protected void doInit() {
        app = (WhatAboutApplication) getApplication();
        repository = (SourceRepository) app.getRepository(io.skysail.server.app.whatAbout.Source.class);
    }

    @Override
    public Set<String> getRestrictedToMediaTypes() {
        return super.getRestrictedToMediaTypes("standalone/*");
    }

    @Override
    public List<io.skysail.server.app.whatAbout.Source> getEntity() {
       return repository.find(new Filter(getRequest()));
    }

    public List<Link> getLinks() {
              return super.getLinks(PostSourceResource.class,SourcesResource.class);
    }
}