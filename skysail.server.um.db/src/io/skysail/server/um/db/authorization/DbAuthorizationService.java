package io.skysail.server.um.db.authorization;

import io.skysail.api.um.*;
import io.skysail.server.um.security.shiro.SkysailHashedCredentialsMatcher;

import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.data.ClientInfo;
import org.restlet.security.*;

@Slf4j
public class DbAuthorizationService implements AuthorizationService, Enroler {

    private DbAuthorizingRealm authorizingRealm;
//    private FileBasedUserManagementProvider userManagementProvider;

    public DbAuthorizationService(UserManagementProvider dbBasedUserManagementProvider) {
        SkysailHashedCredentialsMatcher hashedCredetialsMatcher = new SkysailHashedCredentialsMatcher();
        authorizingRealm = new DbAuthorizingRealm(hashedCredetialsMatcher, null);//simpleUserManagementProvider);
    }
//    public DbAuthorizationService(FileBasedUserManagementProvider simpleUserManagementProvider) {
//        this.userManagementProvider = simpleUserManagementProvider;
//        SkysailHashedCredentialsMatcher hashedCredetialsMatcher = new SkysailHashedCredentialsMatcher();
//
//        hashedCredetialsMatcher.setCacheManager(simpleUserManagementProvider.getCacheManager());
//        authorizingRealm = new DbAuthorizingRealm(hashedCredetialsMatcher,
//                simpleUserManagementProvider);
//    }


    @Override
    public Set<Role> getRolesFor(String principal) {
//        SkysailUser user = userManagementProvider.getByPrincipal(principal);
//        Set<SkysailRole> roles = user.getRoles();
//        if (roles == null) {
//            log.warn("User '" + principal + "' could not be found in the Repository");
//            return Collections.emptySet();
//        }
        return null;// roles.stream().map(r -> getOrCreateRole(r)).collect(Collectors.toSet());
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
        return null;//authorizingRealm;
    }
//
//    private Role getOrCreateRole(SkysailRole r) {
//        RestletRolesProvider restletRolesProvider = userManagementProvider.getRestletRolesProvider();
//        Role role = restletRolesProvider.getRole(r.getName());
//        if (role != null) {
//            return role;
//        }
//        Role newRole = new Role(r.getName());
//        restletRolesProvider.getRoles().add(newRole);
//        return newRole;
//    }

}
