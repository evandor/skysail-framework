package io.skysail.server.um.simple;

import io.skysail.api.um.AuthenticationService;
import io.skysail.api.um.AuthorizationService;
import io.skysail.api.um.UserManagementProvider;
import io.skysail.server.um.simple.authentication.SimpleAuthenticationService;
import io.skysail.server.um.simple.authorization.RestletRolesProvider;
import io.skysail.server.um.simple.authorization.SimpleAuthorizationService;
import io.skysail.server.um.simple.usermanager.UserManagementRepository;
import io.skysail.server.um.simple.web.SimpleWebSecurityManager;

import java.util.Map;
import java.util.Set;

import org.apache.shiro.SecurityUtils;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.ConfigurationPolicy;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;

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
    private RestletRolesProvider restletRolesProvider;

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

    @Reference(dynamic = true, optional = false, multiple = false)
    public void setRestletRolesProvider(RestletRolesProvider restletRolesProvider) {
        this.restletRolesProvider = restletRolesProvider;
    }

    public void unsetRestletRolesProvider(RestletRolesProvider restletRolesProvider) {
        this.restletRolesProvider = null;
    }

    public RestletRolesProvider getRestletRolesProvider() {
        return restletRolesProvider;
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

    public Map<String, Set<String>> getUsernamesAndRoles() {
        return userManagerRepo.getUsernamesAndRoles();
    }
}
