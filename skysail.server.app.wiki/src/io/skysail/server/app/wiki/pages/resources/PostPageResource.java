package io.skysail.server.app.wiki.pages.resources;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.wiki.WikiApplication;
import io.skysail.server.app.wiki.pages.Page;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PostPageResource extends PostEntityServerResource<Page> {
    
    private String spaceId;
    private WikiApplication app;

    public PostPageResource() {
        addToContext(ResourceContextId.LINK_TITLE, "create new Page");
        app = (WikiApplication)getApplication();
    }
    
    @Override
    protected void doInit() throws ResourceException {
        spaceId = getAttribute("id");
    }

    @Override
    public Page createEntityTemplate() {
        return new Page(getQuery(), spaceId);
    }

    public SkysailResponse<?> addEntity(Page entity) {
        Subject subject = SecurityUtils.getSubject();
        entity.setOwner(subject.getPrincipal().toString());
        entity.setSpace(spaceId);
        String id = app.getRepository().add(entity).toString();
        entity.setId(id);
        return new SkysailResponse<String>();
    }
    
    @Override
    public List<Link> getLinks() {
        return super.getLinks(PostPageResource.class);
    }

}
