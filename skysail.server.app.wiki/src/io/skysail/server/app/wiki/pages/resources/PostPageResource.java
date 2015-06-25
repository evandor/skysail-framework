package io.skysail.server.app.wiki.pages.resources;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.wiki.WikiApplication;
import io.skysail.server.app.wiki.pages.Page;
import io.skysail.server.app.wiki.spaces.Space;
import io.skysail.server.app.wiki.spaces.resources.SpacesResource;
import io.skysail.server.app.wiki.versions.Version;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import java.util.*;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PostPageResource extends PostEntityServerResource<Page> {
    
    private String spaceId;
    protected WikiApplication app;
    private Space space;

    public PostPageResource() {
        addToContext(ResourceContextId.LINK_TITLE, "create new Page");
        app = (WikiApplication)getApplication();
    }
    
    @Override
    protected void doInit() throws ResourceException {
        spaceId = getAttribute("id");
        space = app.getRepository().getById(Space.class, spaceId);
        Map<String, String> substitutions = new HashMap<>();
        substitutions.put("/spaces/" + spaceId, space.getName());
        getContext().getAttributes().put(ResourceContextId.PATH_SUBSTITUTION.name(), substitutions);
    }

    @Override
    public Page createEntityTemplate() {
        return new Page();
    }

    public SkysailResponse<?> addEntity(Page entity) {
        Subject subject = SecurityUtils.getSubject();

        Version version = new Version();
        version.setContent(entity.getContent());
        version.setOwner(subject.getPrincipal().toString());
        
        entity.setContent(null);
        entity.addVersion(version);
        
        entity.setOwner(subject.getPrincipal().toString());
        space.addPage(entity);
        app.getRepository().update(spaceId, space);
        return new SkysailResponse<String>();
    }
    
    @Override
    public List<Link> getLinks() {
        return super.getLinks(PostPageResource.class);
    }
    
    @Override
    public String redirectTo() {
        return super.redirectTo(SpacesResource.class);
    }

}
