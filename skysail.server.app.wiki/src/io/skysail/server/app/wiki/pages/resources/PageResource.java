package io.skysail.server.app.wiki.pages.resources;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.wiki.WikiApplication;
import io.skysail.server.restlet.resources.EntityServerResource;

import java.util.*;

public class PageResource extends EntityServerResource<Map<String,Object>> {

    private WikiApplication app;
    private String myEntityId;

    protected void doInit() {
        myEntityId = getAttribute("id");
        app = (WikiApplication) getApplication();
    }

    public Map<String,Object> getEntity() {
        return null;//app.getRepository().getById(Page.class, myEntityId).toMap();
    }

    public List<Link> getLinks() {
         return super.getLinks(PutPageResource.class);
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }



}
