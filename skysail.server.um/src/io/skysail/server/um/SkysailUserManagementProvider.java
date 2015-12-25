package io.skysail.server.um;

import java.util.*;

import aQute.bnd.annotation.component.*;
import de.twenty11.skysail.server.services.UserManager;
import de.twenty11.skysail.server.um.domain.*;
import io.skysail.api.um.*;
import io.skysail.server.db.DbService;
import io.skysail.server.um.security.shiro.*;

//@Component(immediate = true)
// FIXME can be deleted?
public class SkysailUserManagementProvider implements UserManagementProvider, UserManager {

    private ShiroServices authenticationService;
    private DbService dbService;
    private DefaultAuthorizationService authorizationService;
    private UserRepository userRepository;

    @Activate
    public void activate() {
        authenticationService = new ShiroServices(dbService);
        authorizationService = new DefaultAuthorizationService();
        authorizationService.setDbService(dbService);
    }

    @Deactivate
    public void deactivate() {
        authenticationService = null;
        authorizationService = null;
    }

    @Reference(dynamic = true, optional = false, multiple = false)
    public void setDbService(DbService dbService) {
        this.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) {
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
