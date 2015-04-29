package io.skysail.server.app.wiki.pages.resources;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.wiki.WikiApplication;
import io.skysail.server.app.wiki.pages.Page;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import java.util.*;
import java.util.function.Consumer;

public class PutPageResource extends PutEntityServerResource<Map<String, Object>> {

    private String id;

    @Override
    protected void doInit() {
        id = getAttribute("id");
    }

    @Override
    public SkysailResponse<?> updateEntity(Map<String, Object> entity) {
        return null;
    }

    @Override
    public Map<String, Object> getEntity() {
        return ((WikiApplication) getApplication()).getRepository().getById(Page.class, id);
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
