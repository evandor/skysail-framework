package io.skysail.server.app.wiki.spaces.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.wiki.repository.WikiRepository;
import io.skysail.server.app.wiki.spaces.Space;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PostSpaceResource extends PostEntityServerResource<Space> {

    public PostSpaceResource() {
        addToContext(ResourceContextId.LINK_TITLE, "create new Space");
    }
    
    @Override
    public Space createEntityTemplate() {
        return new Space();
    }

    public SkysailResponse<?> addEntity(Space entity) {
        Subject subject = SecurityUtils.getSubject();
        subject.getPrincipals().getPrimaryPrincipal();
        entity.setOwner(subject.getPrincipal().toString());
        String id = WikiRepository.add(entity).toString();
        entity.setId(id);
        return new SkysailResponse<>();        
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(SpacesResource.class);
    }
}
