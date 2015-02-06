package io.skysail.server.um.simple;

import io.skysail.api.um.AuthenticationService;
import io.skysail.api.um.AuthorizationService;
import io.skysail.api.um.UserManagementProvider;
import io.skysail.server.um.simple.authentication.SimpleAuthenticationService;
import io.skysail.server.um.simple.authorization.SimpleAuthorizationService;
import io.skysail.server.um.simple.web.SimpleWebSecurityManager;

import org.apache.shiro.SecurityUtils;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;

@Component(immediate = true)
public class SimpleUserManagementProvider implements UserManagementProvider {

    private SimpleAuthenticationService authenticationService;
    private SimpleAuthorizationService authorizationService;

    @Activate
    public void activate() {
        authenticationService = new SimpleAuthenticationService();
        authorizationService = new SimpleAuthorizationService();
        SecurityUtils.setSecurityManager(new SimpleWebSecurityManager(authorizationService.getRealm()));
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

}
