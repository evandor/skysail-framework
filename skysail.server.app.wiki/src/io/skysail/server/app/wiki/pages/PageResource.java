package io.skysail.server.app.wiki.pages;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.wiki.WikiApplication;
import io.skysail.server.restlet.resources.EntityServerResource;

import java.util.List;
import java.util.Map;

public class PageResource extends EntityServerResource<Map<String,Object>> {

    private WikiApplication app;
    private String myEntityId;

    protected void doInit() {
        myEntityId = getAttribute("id");
        app = (WikiApplication) getApplication();
    }

    public Map<String,Object> getEntity() {
        return app.getRepository().getById(Page.class, myEntityId);
    }

    public List<Link> getLinkheader() {
         return super.getLinkheader(PutPageResource.class);
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }



}
