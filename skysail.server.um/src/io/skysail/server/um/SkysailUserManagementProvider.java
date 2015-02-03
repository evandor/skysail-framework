package io.skysail.server.um;

import io.skysail.api.um.AuthenticationService;
import io.skysail.api.um.AuthorizationService;
import io.skysail.api.um.UserManagementProvider;
import io.skysail.server.um.security.shiro.DefaultAuthorizationService;
import io.skysail.server.um.security.shiro.ShiroServices;
import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;
import de.twenty11.skysail.server.core.db.DbService;

@Component(immediate = true)
public class SkysailUserManagementProvider implements UserManagementProvider {

    private ShiroServices authenticationService;
    private DbService dbService;
    private DefaultAuthorizationService authorizationService;

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

}
