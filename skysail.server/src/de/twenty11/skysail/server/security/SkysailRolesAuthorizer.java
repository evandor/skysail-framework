package de.twenty11.skysail.server.security;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.security.Authorizer;

public class SkysailRolesAuthorizer extends Authorizer {

    private List<String> roleNames;

    public SkysailRolesAuthorizer(List<String> roleNames) {
        this.roleNames = roleNames;
    }

    @Override
    protected boolean authorize(Request request, Response response) {
        // Subject currentUser = (Subject) request.getAttributes().get("subject");
        Subject currentUser = SecurityUtils.getSubject();
        if (!currentUser.isAuthenticated()) {
            return false;
        }
        return currentUser.hasAllRoles(roleNames);
    }

    @Override
    public String toString() {
        return "SkysailRolesAuthorizer (" + roleNames + ")";
    }
}
