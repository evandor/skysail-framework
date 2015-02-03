package io.skysail.server.um.security.shiro;

import io.skysail.api.um.AuthenticationService;
import io.skysail.api.um.User;
import io.skysail.server.um.security.shiro.mgt.SkysailWebSecurityManager;
import io.skysail.server.um.security.shiro.restlet.ShiroDelegationAuthenticator;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.SimplePrincipalMap;
import org.apache.shiro.subject.Subject;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.security.Authenticator;

import de.twenty11.skysail.server.core.db.DbService;

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
    public void logout() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            subject.logout();
        }
    }

    // @Override
    // public boolean validateAuthToken(UsernamePasswordToken authToken) {
    // AuthenticationInfo authenticationInfo =
    // authorizingRealm.getAuthenticationInfo(authToken);
    // return false;
    // }

    @Override
    public Authenticator getAuthenticator(Context context) {
        // https://github.com/qwerky/DataVault/blob/master/src/qwerky/tools/datavault/DataVault.java
        return new ShiroDelegationAuthenticator(context, SKYSAIL_SHIRO_DB_REALM, "thisHasToBecomeM".getBytes());
    }

    @Override
    public User getCurrentUser() {
        Subject subject = SecurityUtils.getSubject();
        if (subject == null) {
            return null;
        }
        return new User((String) subject.getPrincipal(), null);
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
    public boolean authenticate(Request request, Response response) {
        // TODO Auto-generated method stub
        return false;
    }

}
