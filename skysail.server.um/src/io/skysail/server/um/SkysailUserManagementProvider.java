package io.skysail.server.um;

import io.skysail.api.um.AuthenticationService;
import io.skysail.api.um.AuthorizationService;
import io.skysail.api.um.UserManagementProvider;
import io.skysail.server.db.DbService2;
import io.skysail.server.um.security.shiro.DefaultAuthorizationService;
import io.skysail.server.um.security.shiro.ShiroServices;
import io.skysail.server.um.security.shiro.UserRepository;

import java.util.Collections;
import java.util.List;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;
import de.twenty11.skysail.server.services.UserManager;
import de.twenty11.skysail.server.um.domain.SkysailGroup;
import de.twenty11.skysail.server.um.domain.SkysailUser;

@Component(immediate = true)
public class SkysailUserManagementProvider implements UserManagementProvider, UserManager {

    private ShiroServices authenticationService;
    private DbService2 dbService;
    private DefaultAuthorizationService authorizationService;
    private UserRepository userRepository;

    @Activate
    public void activate() {
        authenticationService = new ShiroServices(dbService);
        authorizationService = new DefaultAuthorizationService();
    }

    @Deactivate
    public void deactivate() {
        authenticationService = null;
        authorizationService = null;
    }

    @Reference(dynamic = true, optional = false, multiple = false)
    public void setDbService(DbService2 dbService) {
        this.dbService = dbService;
    }

    public void unsetDbService(DbService2 dbService) {
        this.dbService = null;
    }

    @Override
    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    @Override
    public AuthorizationService getAuthorizationService() {
        return authorizationService;
    }

    public synchronized UserRepository getUserRepository() {
        if (this.userRepository == null) {
            this.userRepository = new UserRepository(dbService);
        }
        return this.userRepository;
    }

    @Override
    public SkysailUser findByUsername(String username) {
        return getUserRepository().getByName(username);
    }

    @Override
    public SkysailUser findById(String id) {
        return getUserRepository().getById(id);
    }

    @Override
    public List<SkysailGroup> getGroupsForUser(String username) {
        return Collections.emptyList();// getUserRepository().getGroupsForUser(username);
    }

    @Override
    public void update(SkysailUser user) {
        getUserRepository().update(user);

    }

}
