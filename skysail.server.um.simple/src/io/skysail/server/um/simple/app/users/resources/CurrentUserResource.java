package io.skysail.server.um.simple.app.users.resources;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import de.twenty11.skysail.server.um.domain.SkysailUser;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;


public class CurrentUserResource extends EntityServerResource<SkysailUser> {
    
    public CurrentUserResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Show current user");
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public SkysailUser getEntity() {
        Subject subject = SecurityUtils.getSubject();
        Object principal = subject.getPrincipal();
        return new SkysailUser(principal == null ? null : principal.toString(), null, null);
    }


}
