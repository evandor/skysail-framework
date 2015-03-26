package io.skysail.server.um.simple.authorization;

import io.skysail.api.um.AuthorizationService;
import io.skysail.api.um.RestletRolesProvider;
import io.skysail.server.um.simple.FileBasedUserManagementProvider;
import io.skysail.server.um.simple.authentication.SkysailHashedCredentialsMatcher;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.Subject;
import org.restlet.data.ClientInfo;
import org.restlet.security.Enroler;
import org.restlet.security.Role;

import de.twenty11.skysail.server.um.domain.SkysailRole;
import de.twenty11.skysail.server.um.domain.SkysailUser;

@Slf4j
public class SimpleAuthorizationService implements AuthorizationService, Enroler {

    private SimpleAuthorizingRealm authorizingRealm;
    private FileBasedUserManagementProvider userManagementProvider;

    public SimpleAuthorizationService(FileBasedUserManagementProvider simpleUserManagementProvider) {
        this.userManagementProvider = simpleUserManagementProvider;
        SkysailHashedCredentialsMatcher hashedCredetialsMatcher = new SkysailHashedCredentialsMatcher();
        
        hashedCredetialsMatcher.setCacheManager(simpleUserManagementProvider.getCacheManager());
        authorizingRealm = new SimpleAuthorizingRealm(hashedCredetialsMatcher,
                simpleUserManagementProvider);
    }

    @Override
    public Set<Role> getRolesFor(String principal) {
        SkysailUser user = userManagementProvider.getByPrincipal(principal);
        Set<SkysailRole> roles = user.getRoles();
        if (roles == null) {
            log.warn("User '" + principal + "' could not be found in the Repository");
            return Collections.emptySet();
        }
        return roles.stream().map(r -> getOrCreateRole(r)).collect(Collectors.toSet());
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
        Set<Role> userRoles = findRoles(subject);

        for (Role role : userRoles) {
            clientInfo.getRoles().add(role);
        }

        // Add roles common to group members
        // Set<Role> groupRoles = findRoles(userGroups);

        // for (Role role : groupRoles) {
        // clientInfo.getRoles().add(role);
        // }

    }

    private Set<Role> findRoles(Subject subject) {
        return getRolesFor((String) subject.getPrincipal());
    }

    public Realm getRealm() {
        return authorizingRealm;
    }

    private Role getOrCreateRole(SkysailRole r) {
        RestletRolesProvider restletRolesProvider = userManagementProvider.getRestletRolesProvider();
        Role role = restletRolesProvider.getRole(r.getName());
        if (role != null) {
            return role;
        }
        Role newRole = new Role(r.getName());
        restletRolesProvider.getRoles().add(newRole);
        return newRole;
    }

}
