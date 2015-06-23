package io.skysail.server.app.wiki.pages.resources;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.wiki.WikiApplication;
import io.skysail.server.app.wiki.pages.Page;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import java.util.List;
import java.util.function.Consumer;

public class PutPageResource extends PutEntityServerResource<Page> {

    private String id;
    private WikiApplication app;
    private String pageId;

    @Override
    protected void doInit() {
        id = getAttribute("id");
        pageId = getAttribute("pageId");
        app = (WikiApplication)getApplication();
    }

    @Override
    public SkysailResponse<?> updateEntity(Page entity) {
        return null;
    }

    @Override
    public Page getEntity() {
        return app.getRepository().getById(Page.class, pageId);
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(PutPageResource.class);
    }

    @Override
    public Consumer<? super Link> getPathSubstitutions() {
        return l -> {
            l.substitute("id", id);
        };
    }
}
