package io.skysail.server.um.db;

import io.skysail.api.um.*;
import io.skysail.server.um.db.authentication.DbAuthenticationService;
import io.skysail.server.um.db.authorization.DbAuthorizationService;
import io.skysail.server.um.security.shiro.mgt.SkysailWebSecurityManager;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.apache.shiro.SecurityUtils;

import aQute.bnd.annotation.component.*;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.optional)
@Slf4j
public class DbBasedUserManagementProvider implements UserManagementProvider {

    private DbAuthorizationService authorizationService;
    private DbAuthenticationService authenticationService;

    @Activate
    public void activate(Map<String, String> config) {
        // userManagerRepo = new UserManagementRepository(config);
        authenticationService = new DbAuthenticationService(this);
        authorizationService = new DbAuthorizationService(this);
        SecurityUtils.setSecurityManager(new SkysailWebSecurityManager());//authorizationService.getRealm()));
    }

    @Deactivate
    public void deactivate() {
        // authenticationService = null;
         authorizationService = null;
        SecurityUtils.setSecurityManager(null);
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
