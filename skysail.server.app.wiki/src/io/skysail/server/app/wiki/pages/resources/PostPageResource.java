package io.skysail.server.app.wiki.pages.resources;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.wiki.*;
import io.skysail.server.app.wiki.pages.Page;
import io.skysail.server.app.wiki.spaces.Space;

import java.util.*;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PostPageResource extends PostDynamicEntityServerResource<Page> {
    
    private String id;
    private WikiApplication app;

    public PostPageResource() {
        addToContext(ResourceContextId.LINK_TITLE, "create new Page");
        app = (WikiApplication)getApplication();
    }
    
    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
    }

    @Override
    public Page createEntityTemplate() {
        return new Page();
    }

    // TODO run in one transaction
    public SkysailResponse<?> addEntity(Page entity) {
        Object pageId = app.getRepository().add(entity);
        Map<String, Object> space = app.getRepository().getById(Space.class, id);
        Set<String> pageSet = (Set<String>)space.get("Page");
        if (pageSet == null) {
            pageSet = new HashSet<String>();
        }
        pageSet.add(pageId.toString());
        app.getRepository().update(space);
        
//        Space space = (Space)app.getRepository().getObjectById(Space.class, id);
//        List<Page> pages = (List<Page>)space.getInstance().get("Page");
//        if (pages == null) {
//            pages = new ArrayList<Page>();
//        }
//        pages.add(entity);
//        space.getInstance().set("Page", pages);
//        app.getRepository().update(id, space);
        return new SkysailResponse<String>();
    }
    
    @Override
    public List<Link> getLinks() {
        return super.getLinks(PostPageResource.class);
    }

}
