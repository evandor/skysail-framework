package io.skysail.server.app.wiki.pages.resources;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.wiki.PostDynamicEntityServerResource;
import io.skysail.server.app.wiki.WikiApplication;
import io.skysail.server.app.wiki.pages.Page;
import io.skysail.server.app.wiki.spaces.Space;

import java.util.ArrayList;
import java.util.List;

import org.restlet.resource.ResourceException;

import com.orientechnologies.orient.core.record.impl.ODocument;

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

    public SkysailResponse<?> addEntity(Page entity) {
        ODocument space = app.getRepository().getById(Space.class, id);
        List<ODocument> pages = space.field(Page.class.getSimpleName());
        if (pages == null) {
            pages = new ArrayList<ODocument>();
        }
        pages.add(new ODocument(Page.class.getSimpleName()).field("title", entity.getInstance().get("title")));
        space.field("Page", pages);
        app.getRepository().updateDocument(space);
        return new SkysailResponse<String>();
    }
    
    @Override
    public List<Link> getLinks() {
        return super.getLinks(PostPageResource.class);
    }

}
