package io.skysail.server.app.wiki.spaces.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.wiki.WikiApplication;
import io.skysail.server.app.wiki.spaces.Space;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PostSpaceResource extends PostEntityServerResource<Space> {

    private WikiApplication app;

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        app = (WikiApplication)getApplication();
    }

    public PostSpaceResource() {
        addToContext(ResourceContextId.LINK_TITLE, "create new Space");
    }

    @Override
    public Space createEntityTemplate() {
        return new Space();
    }

    public SkysailResponse<Space> addEntity(Space entity) {
        Subject subject = SecurityUtils.getSubject();
        subject.getPrincipals().getPrimaryPrincipal();
        entity.setOwner(subject.getPrincipal().toString());
        //String id = WikiRepository.add(entity).toString();
        String id = app.getSpacesRepo().save(entity).toString();
        entity.setId(id);
        return new SkysailResponse<>();
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(SpacesResource.class);
    }
}
