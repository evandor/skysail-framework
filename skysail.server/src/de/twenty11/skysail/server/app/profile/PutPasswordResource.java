package de.twenty11.skysail.server.app.profile;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.api.um.User;

import org.apache.shiro.SecurityUtils;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.app.SkysailRootApplication;
import de.twenty11.skysail.server.core.restlet.PutEntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PutPasswordResource extends PutEntityServerResource<ChangePasswordEntity> {

    private SkysailRootApplication app;

    public PutPasswordResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Update Password");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (SkysailRootApplication) getApplication();
    }

    @Override
    public SkysailResponse<?> updateEntity(ChangePasswordEntity entity) {
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        User user = new User(username, entity.getOld());
        app.getAuthenticationService().updatePassword(user, entity.getPassword());
        return new SkysailResponse<String>();
    }

    @Override
    public String redirectTo() {
        if (getResponse().getStatus().isSuccess()) {
            return SkysailRootApplication.LOGOUT_PATH;
        }
        return null;
    }

    @Override
    public ChangePasswordEntity getEntity() {
        return new ChangePasswordEntity();
    }

}
