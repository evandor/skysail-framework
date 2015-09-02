package io.skysail.server.um.security.shiro;

import io.skysail.api.um.AuthenticationService;
import io.skysail.api.um.User;
import io.skysail.server.db.DbService;
import io.skysail.server.um.security.shiro.mgt.SkysailWebSecurityManager;
import io.skysail.server.um.security.shiro.restlet.ShiroDelegationAuthenticator;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.SimplePrincipalMap;
import org.restlet.Context;
import org.restlet.security.Authenticator;

/**
 * Default AuthenticationService Implementation shipped with skysail
 * 
 */
@Slf4j
public class ShiroServices implements AuthenticationService {

    public static final String SKYSAIL_SHIRO_DB_REALM = "skysail.shiro.db.realm";

    private volatile SkysailAuthorizingRealm authorizingRealm;

    @Getter
    private DbService dbService;

    public ShiroServices(DbService dbService) {
        this.dbService = dbService;
        log.info("creating new SkysailAuthorizingRealm...");
        authorizingRealm = new SkysailAuthorizingRealm(new SkysailHashedCredentialsMatcher(), this);

        log.info("Setting new SkysailWebSecurityManager as Shiros SecurityManager");
        SecurityUtils.setSecurityManager(new SkysailWebSecurityManager(authorizingRealm));
    }

    @Override
    public Authenticator getAuthenticator(Context context) {
        // https://github.com/qwerky/DataVault/blob/master/src/qwerky/tools/datavault/DataVault.java
        return new ShiroDelegationAuthenticator(context, SKYSAIL_SHIRO_DB_REALM, "thisHasToBecomeM".getBytes());
    }

    @Override
    public void clearCache(String username) {
        SimplePrincipalMap principalsMap = new SimplePrincipalMap();
        Map<String, Object> principals = new HashMap<>();
        principals.put(username, "");
        principalsMap.put(SkysailAuthorizingRealm.class.getSimpleName(), username);
        authorizingRealm.clearCache(principalsMap);
    }

    @Override
    public void updatePassword(User user, String newPassword) {
        throw new UnsupportedOperationException("not yet implemented");
    }

}
