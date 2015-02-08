package io.skysail.server.um.simple;

import io.skysail.api.um.AuthenticationService;
import io.skysail.api.um.AuthorizationService;
import io.skysail.api.um.UserManagementProvider;
import io.skysail.server.um.simple.authentication.SimpleAuthenticationService;
import io.skysail.server.um.simple.authorization.SimpleAuthorizationService;
import io.skysail.server.um.simple.usermanager.UserManagementRepository;
import io.skysail.server.um.simple.web.SimpleWebSecurityManager;

import java.util.Map;

import org.apache.shiro.SecurityUtils;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.ConfigurationPolicy;
import aQute.bnd.annotation.component.Deactivate;

/**
 * A UserManagerProvider based on a configuration file containing information
 * about existing users, their passwords and roles.
 *
 */
@Component(immediate = true, configurationPolicy = ConfigurationPolicy.require)
public class SimpleUserManagementProvider implements UserManagementProvider {

    private SimpleAuthenticationService authenticationService;
    private SimpleAuthorizationService authorizationService;

    private UserManagementRepository userManagerRepo;

    @Activate
    public void activate(Map<String, String> config) {
        authenticationService = new SimpleAuthenticationService();
        authorizationService = new SimpleAuthorizationService(this);
        SecurityUtils.setSecurityManager(new SimpleWebSecurityManager(authorizationService.getRealm()));
        userManagerRepo = new UserManagementRepository(config);
    }

    @Deactivate
    public void deactivate() {
        authenticationService = null;
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

    public Map<String, String> getUsernamesAndPasswords() {
        return userManagerRepo.getUsernamesAndPasswords();
    }
}
