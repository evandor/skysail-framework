package io.skysail.server.um.security.shiro;

import io.skysail.api.um.AuthorizationService;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.data.ClientInfo;
import org.restlet.security.Enroler;
import org.restlet.security.Role;

import aQute.bnd.annotation.component.Reference;
import de.twenty11.skysail.server.core.db.DbService;
import de.twenty11.skysail.server.um.domain.SkysailRole;
import de.twenty11.skysail.server.um.domain.SkysailUser;

@Slf4j
public class DefaultAuthorizationService implements AuthorizationService, Enroler {

    private UserRepository userRepository;
    private RestletRolesProvider restletRolesProvider;
    private DbService dbService;

    @Reference
    public synchronized void setDbService(DbService service) {
        this.dbService = service;
    }

    public synchronized void unsetEntityManager(@SuppressWarnings("unused") DbService service) {
        this.dbService = null;
    }

    @Override
    public Set<Role> getRolesFor(String username) {
        SkysailUser user = getUserRepository().getByName(username);
        if (user == null) {
            log.warn("User '" + username + "' could not be found in the Repository");
            return Collections.emptySet();
        }
        return user.getRoles().stream().map(r -> getOrCreateRole(r)).collect(Collectors.toSet());
    }

    @Reference(dynamic = true, multiple = false, optional = false)
    public synchronized void setRestletRolesProvider(RestletRolesProvider rrp) {
        this.restletRolesProvider = rrp;
    }

    public synchronized void unsetRestletRolesProvider(RestletRolesProvider rrp) {
        this.restletRolesProvider = null;
    }

    public synchronized UserRepository getUserRepository() {
        if (this.userRepository == null) {
            this.userRepository = new UserRepository(dbService);
        }
        return this.userRepository;
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

    private Role getOrCreateRole(SkysailRole r) {
        if (restletRolesProvider == null || r == null) {
            throw new NullPointerException();
        }
        Role role = restletRolesProvider.getRole(r.getName());
        if (role != null) {
            return role;
        }
        Role newRole = new Role(r.getName());
        restletRolesProvider.getRoles().add(newRole);
        return newRole;
    }

}
