package de.twenty11.skysail.server.app.profile;

import org.apache.shiro.SecurityUtils;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.app.SkysailRootApplication;
import de.twenty11.skysail.server.core.restlet.PutEntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import de.twenty11.skysail.server.um.domain.SkysailUser;
import de.twenty11.skysail.server.utils.PasswordUtils;

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
    public ChangePasswordEntity getEntity() {
        return new ChangePasswordEntity();
    }

    @Override
    public SkysailResponse<?> updateEntity(ChangePasswordEntity entity) {
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        SkysailUser currentUser = app.getUserManager().findByUsername(username);

        if (!PasswordUtils.validate(entity.getOld(), currentUser.getPassword())) {
            getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            return null;
        }

        String bCryptHash = PasswordUtils.createBCryptHash(entity.getPassword());
        currentUser.setPassword(bCryptHash);
        app.getUserManager().update(currentUser);
        // SecurityUtils.getSecurityManager().logout(SecurityUtils.getSubject());
        app.clearCache(username);
        return new SkysailResponse<String>();
    }

    @Override
    public String redirectTo() {
        if (getResponse().getStatus().isSuccess()) {
            return SkysailRootApplication.LOGOUT_PATH;
        }
        return null;
    }
    

}
