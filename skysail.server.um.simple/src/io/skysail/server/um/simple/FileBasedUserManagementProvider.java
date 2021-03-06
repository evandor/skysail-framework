package io.skysail.server.um.simple;

import java.io.IOException;
import java.util.*;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.cache.*;
import org.osgi.service.cm.*;
import org.osgi.service.component.annotations.*;
import org.osgi.service.event.EventAdmin;

import de.twenty11.skysail.server.um.domain.SkysailUser;
import io.skysail.api.um.*;
import io.skysail.server.um.simple.authentication.SimpleAuthenticationService;
import io.skysail.server.um.simple.authorization.SimpleAuthorizationService;
import io.skysail.server.um.simple.repository.UserManagementRepository;
import io.skysail.server.um.simple.web.impl.SkysailWebSecurityManager;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * A UserManagerProvider based on a configuration file (containing information
 * about existing users, their ids, passwords and roles).
 * 
 * <p>
 * Delegates to various services and creates a default configuration if no other
 * configuration is provided.
 * </p>
 *
 */
@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
@Slf4j
public class FileBasedUserManagementProvider implements UserManagementProvider {

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    @Getter
    private volatile EventAdmin eventAdmin;

    @Getter
    private volatile SimpleAuthenticationService authenticationService;

    @Getter
    private volatile SimpleAuthorizationService authorizationService;

    @Getter
    private volatile RestletRolesProvider restletRolesProvider;

    private volatile UserManagementRepository userManagerRepo;
    
    private volatile ConfigurationAdmin configurationAdmin;

    @Getter
    private CacheManager cacheManager;

    @Activate
    public void activate(Map<String, String> config) {

        if (config.get("users") == null) {
            createDefautConfiguration();
            return;
        }
        cacheManager = new MemoryConstrainedCacheManager();
        userManagerRepo = new UserManagementRepository(config);
        authenticationService = new SimpleAuthenticationService(this);
        authorizationService = new SimpleAuthorizationService(this);
        SecurityUtils.setSecurityManager(new SkysailWebSecurityManager(authorizationService.getRealm()));
    }

    @Deactivate
    public void deactivate() {
        authenticationService = null;
        authorizationService = null;
        SecurityUtils.setSecurityManager(null);
        cacheManager = null;
    }

    // --- ConfigurationAdmin ------------------------------------------------
    @Reference(policy =  ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY)
    public void setConfigurationAdmin(ConfigurationAdmin configurationAdmin) {
        this.configurationAdmin = configurationAdmin;
    }

    public void unsetConfigurationAdmin(ConfigurationAdmin configurationAdmin) {
        this.configurationAdmin = null;
    }

    // --- RestletRolesProvider -----------------------------------------------
    @Reference(policy =  ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY)
    public void setRestletRolesProvider(RestletRolesProvider restletRolesProvider) {
        this.restletRolesProvider = restletRolesProvider;
    }

    public void unsetRestletRolesProvider(RestletRolesProvider restletRolesProvider) {
        this.restletRolesProvider = null;
    }

    public SkysailUser getByUsername(String username) {
        return userManagerRepo.getByUsername(username);
    }

    public SkysailUser getByPrincipal(String username) {
        return userManagerRepo.getByPrincipal(username);
    }

    private void createDefautConfiguration() {
        log.warn("creating default configuration for usermanagement as no configuration was provided (yet)!");
        try {
            Configuration config = configurationAdmin.getConfiguration(this.getClass().getName());
            Dictionary<String, Object> props = config.getProperties();
            if (props == null) {
                props = new Hashtable<String, Object>();
            }
            props.put("users", "admin,user");
            props.put("service.pid", this.getClass().getName());

            props.put("admin.password", "$2a$12$52R8v2QH3vQRz8NcdtOm5.HhE5tFPZ0T/.MpfUa9rBzOugK.btAHS");
            props.put("admin.roles", "admin,developer");
            props.put("admin.id", "#1");

            props.put("user.password", "$2a$12$52R8v2QH3vQRz8NcdtOm5.HhE5tFPZ0T/.MpfUa9rBzOugK.btAHS");
            props.put("user.id", "#2");

            config.update(props);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

    }


}
