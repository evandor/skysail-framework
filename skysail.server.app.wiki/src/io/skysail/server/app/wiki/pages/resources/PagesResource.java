package io.skysail.server.app.wiki.pages.resources;

import io.skysail.api.links.Link;
import io.skysail.server.app.wiki.WikiApplication;
import io.skysail.server.app.wiki.pages.Page;
import io.skysail.server.app.wiki.spaces.Space;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PagesResource extends ListServerResource<Page> {

    //public static final String DEFAULT_FILTER_EXPRESSION = "(!(status="+Status.ARCHIVED+"))";

    private String spaceId;
    protected WikiApplication app;

    public PagesResource() {
        super(PageResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list Pages");
    }

    @Override
    protected void doInit() throws ResourceException {
        spaceId = getAttribute("id");
        app = (WikiApplication) getApplication();
    }

    @Override
    public List<Page> getEntity() {
        Filter filter = new Filter(getRequest());
        filter.add("owner",  SecurityUtils.getSubject().getPrincipal().toString());
        filter.add("space", spaceId);
        
       
        //Pagination pagination = new Pagination(getRequest(), getResponse(), app.getRepository().getTodosCount(spaceId, filter));
        //return app.getRepository().findAllPages2(filter);
        Space space = app.getRepository().getById(Space.class, spaceId);
        return space.getPages();
        
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(PagesResource.class, PostPageResource.class); 
    }
    
//    @Override
//    public Consumer<? super Link> getPathSubstitutions() {
//        return l -> l.substitute("id", spaceId);
//    }
}
