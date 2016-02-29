package io.skysail.server.app.wiki;

import io.skysail.server.ResourceContextId;
import io.skysail.server.db.DbClassName;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.api.links.Link;

import java.util.List;
import java.util.Map;

public class PagesResource extends ListServerResource<io.skysail.server.app.wiki.Page> {

    private WikiApplication app;

    public PagesResource() {
        super(PageResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list Pages");
    }

    @Override
    protected void doInit() {
        app = (WikiApplication) getApplication();
    }

    @Override
    public List<?> getEntity() {
       //return repository.find(new Filter(getRequest()));
        String sql = "SELECT from " + DbClassName.of(Page.class) + " WHERE #" + getAttribute("id") + " IN in('pages')";
        return ((SpaceRepository)app.getRepository(Space.class)).execute(Page.class, sql);   
    }

    public List<Link> getLinks() {
       return super.getLinks(PostPageResource.class);
    }
}