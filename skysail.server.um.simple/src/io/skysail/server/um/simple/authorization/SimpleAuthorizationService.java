package io.skysail.server.um.simple.authorization;

import io.skysail.api.um.AuthorizationService;
import io.skysail.server.um.simple.SimpleUserManagementProvider;
import io.skysail.server.um.simple.authentication.SkysailHashedCredentialsMatcher;

import java.util.Collections;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.Subject;
import org.restlet.data.ClientInfo;
import org.restlet.security.Enroler;
import org.restlet.security.Role;

public class SimpleAuthorizationService implements AuthorizationService, Enroler {

    private SimpleAuthorizingRealm authorizingRealm;

    public SimpleAuthorizationService(SimpleUserManagementProvider simpleUserManagementProvider) {
        authorizingRealm = new SimpleAuthorizingRealm(new SkysailHashedCredentialsMatcher(),
                simpleUserManagementProvider);
    }

    @Override
    public Set<Role> getRolesFor(String username) {
        return Collections.emptySet();
    }

    @Override
    public void enrole(ClientInfo clientInfo) {
        Subject subject = SecurityUtils.getSubject();
        if (subject == null) {
            return;
        }
        // Find all the inherited groups of this user
        // Set<Group> userGroups = findGroups(user);

        // Add roles specific to this user
        Set<Role> userRoles = Collections.emptySet();// findRoles(subject);

        for (Role role : userRoles) {
            clientInfo.getRoles().add(role);
        }

        // Add roles common to group members
        // Set<Role> groupRoles = findRoles(userGroups);

        // for (Role role : groupRoles) {
        // clientInfo.getRoles().add(role);
        // }

    }

    public Realm getRealm() {
        return authorizingRealm;
    }

}
