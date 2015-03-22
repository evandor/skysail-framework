package io.skysail.server.um.simple;

import io.skysail.api.um.AuthenticationService;
import io.skysail.api.um.AuthorizationService;
import io.skysail.api.um.UserManagementProvider;
import io.skysail.server.um.simple.authentication.SimpleAuthenticationService;
import io.skysail.server.um.simple.authorization.RestletRolesProvider;
import io.skysail.server.um.simple.authorization.SimpleAuthorizationService;
import io.skysail.server.um.simple.authorization.SimpleUser;
import io.skysail.server.um.simple.usermanager.UserManagementRepository;
import io.skysail.server.um.simple.web.impl.SimpleWebSecurityManager;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.apache.shiro.SecurityUtils;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.ConfigurationPolicy;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;

/**
 * A UserManagerProvider based on a configuration file containing information
 * about existing users, their ids, passwords and roles.
 * 
 * Delegates to various services and creates a default configuration if no other
 * configuration is provided.
 *
 */
@Component(immediate = true, configurationPolicy = ConfigurationPolicy.optional)
@Slf4j
public class SimpleUserManagementProvider implements UserManagementProvider {

    private volatile SimpleAuthenticationService authenticationService;
    private volatile SimpleAuthorizationService authorizationService;
    private volatile UserManagementRepository userManagerRepo;
    private volatile RestletRolesProvider restletRolesProvider;
    private volatile ConfigurationAdmin configurationAdmin;

    @Activate
    public void activate(Map<String, String> config) {
        if (config.get("users") == null) {
            createDefautConfiguration();
            return;
        }
        userManagerRepo = new UserManagementRepository(config);
        authenticationService = new SimpleAuthenticationService();
        authorizationService = new SimpleAuthorizationService(this);
        SecurityUtils.setSecurityManager(new SimpleWebSecurityManager(authorizationService.getRealm()));
    }

    @Deactivate
    public void deactivate() {
        authenticationService = null;
        authorizationService = null;
        SecurityUtils.setSecurityManager(null);
    }

    // --- ConfigurationAdmin ------------------------------------------------

    @Reference(dynamic = true, optional = false, multiple = false)
    public void setConfigurationAdmin(ConfigurationAdmin configurationAdmin) {
        this.configurationAdmin = configurationAdmin;
    }

    public void unsetConfigurationAdmin(ConfigurationAdmin configurationAdmin) {
        this.configurationAdmin = null;
    }

    // --- RestletRolesProvider -----------------------------------------------

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

    public SimpleUser getByUsername(String username) {
        return userManagerRepo.getByUsername(username);
    }

    public SimpleUser getByPrincipal(String username) {
        return userManagerRepo.getByPrincipal(username);
    }

    private void createDefautConfiguration() {
        log.warn("creating default configuration for usermanagement as no configuration was provided!");
        try {
            Configuration config = configurationAdmin.getConfiguration(this.getClass().getName());
            Dictionary<String, Object> props = config.getProperties();
            if (props == null) {
                props = new Hashtable<String, Object>();
            }
            props.put("users", "admin,user,sysadmin");
            props.put("service.pid", this.getClass().getName());

            props.put("admin.password", "$2a$12$52R8v2QH3vQRz8NcdtOm5.HhE5tFPZ0T/.MpfUa9rBzOugK.btAHS");
            props.put("admin.roles", "admin");
            props.put("admin.id", "#1");

            config.update(props);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

    }

}
