package io.skysail.server.app.wiki.spaces.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.wiki.WikiApplication;
import io.skysail.server.app.wiki.repository.WikiRepository;
import io.skysail.server.app.wiki.spaces.Space;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PostSpaceResource extends PostEntityServerResource<Space> {

    private WikiApplication app;

    public PostSpaceResource() {
        addToContext(ResourceContextId.LINK_TITLE, "create new Space");
    }
    
    @Override
    protected void doInit() throws ResourceException {
        app = (WikiApplication)getApplication();
    }

    @Override
    public Space createEntityTemplate() {
        return new Space();
    }

    public SkysailResponse<?> addEntity(Space entity) {
//        entity.setCreated(new Date());
        Subject subject = SecurityUtils.getSubject();
        subject.getPrincipals().getPrimaryPrincipal();
        entity.setOwner(subject.getPrincipal().toString());
        String id = WikiRepository.add(entity).toString();
        entity.setId(id);
        return new SkysailResponse<>();        
    }

}
