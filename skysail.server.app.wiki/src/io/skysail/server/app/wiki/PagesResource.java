package io.skysail.server.app.wiki;

import java.util.List;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.api.links.Link;
import io.skysail.server.db.DbClassName;
import io.skysail.server.restlet.resources.ListServerResource;

public class PagesResource extends ListServerResource<io.skysail.server.app.wiki.Page> {

    private WikiApplication app;
    //private PageRepository repository;

    public PagesResource() {
        super(PageResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list Pages");
    }

    @Override
    protected void doInit() {
        app = (WikiApplication) getApplication();
        //repository = (PageRepository) app.getRepository(io.skysail.server.app.wiki.Page.class);
    }

    @Override
    public List<?> getEntity() {
        String sql = "SELECT from " + DbClassName.of(Page.class) + " WHERE #" + getAttribute("id") + " IN in('pages')";
        return ((SpaceRepository)app.getRepository(Space.class)).execute(Page.class, sql);   
    }

    public List<Link> getLinks() {
       return super.getLinks(PostPageResource.class);
    }
}