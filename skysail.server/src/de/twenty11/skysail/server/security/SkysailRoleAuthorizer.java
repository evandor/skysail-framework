package de.twenty11.skysail.server.security;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.security.Authorizer;

public class SkysailRoleAuthorizer extends Authorizer {

    public SkysailRoleAuthorizer(String roleName) {
        super(roleName);
    }

    @Override
    protected boolean authorize(Request request, Response response) {
        // Subject currentUser = (Subject) request.getAttributes().get("subject");
        Subject currentUser = SecurityUtils.getSubject();
        if (getIdentifier() == null) {
            return true;
        }
        if (!currentUser.isAuthenticated()) {
            return false;
        }
        return currentUser.hasRole(getIdentifier());
    }

    @Override
    public String toString() {
        return "SkysailRoleAuthorizer (" + getIdentifier() + ")";
    }
}
