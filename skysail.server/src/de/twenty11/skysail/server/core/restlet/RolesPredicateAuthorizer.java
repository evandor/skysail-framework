package de.twenty11.skysail.server.core.restlet;

import java.util.List;
import java.util.stream.Collectors;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.security.Authorizer;
import org.restlet.security.Role;

import com.google.common.base.Predicate;

public class RolesPredicateAuthorizer extends Authorizer  {

    private Predicate<String[]> rolesPredicate;

    /**
     * @param rolesPredicate predicate describing the logic for the needed roles.
     */
    public RolesPredicateAuthorizer(Predicate<String[]> rolesPredicate) {
        this.rolesPredicate = rolesPredicate;
    }

    @Override
    protected boolean authorize(Request request, Response response) {
        List<Role> clientRoles = request.getClientInfo().getRoles();
        if (clientRoles == null && rolesPredicate == null) {
            return true;
        }
        if (rolesPredicate == null) {
            return true;
        }
        List<String> clientRoleNames = clientRoles.stream().map(cR -> cR.getName()).collect(Collectors.toList());
        return rolesPredicate.apply(clientRoleNames.toArray(new String[clientRoleNames.size()]));
    }
    
    @Override
    public String toString() {
        return new StringBuilder(getClass().getSimpleName()).append(" ").append(rolesPredicate).toString();
    }

}
