package io.skysail.server.app.wiki;

import io.skysail.server.queryfilter.Filter;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.api.links.Link;

import java.util.List;
import java.util.Map;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PagesResource extends ListServerResource<io.skysail.server.app.wiki.Page> {

    private WikiApplication app;
    private PageRepository repository;

    public PagesResource() {
        super(PageResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list Pages");
    }

    @Override
    protected void doInit() {
        app = (WikiApplication) getApplication();
        repository = (PageRepository) app.getRepository(io.skysail.server.app.wiki.Page.class);
    }

    @Override
    public List<io.skysail.server.app.wiki.Page> getEntity() {
       return repository.find(new Filter(getRequest()));
    }

    public List<Link> getLinks() {
       return super.getLinks(PostPageResource.class);
    }
}