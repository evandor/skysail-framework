package io.skysail.server.um.simple.authorization;

import io.skysail.api.um.AuthorizationService;
import io.skysail.server.um.simple.SkysailAuthenticationInfo;
import io.skysail.server.um.simple.SkysailHashedCredentialsMatcher;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.MapCache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import de.twenty11.skysail.server.um.domain.SkysailUser;

public class SimpleAuthorizingRealm extends AuthorizingRealm {

    private AuthorizationService authenticationSerivce;
    private static Map<String, String> usernamesAndPasswords = new HashMap<>();

    static {
        usernamesAndPasswords.put("admin", "$2a$12$52R8v2QH3vQRz8NcdtOm5.HhE5tFPZ0T/.MpfUa9rBzOugK.btAHS");
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    public SimpleAuthorizingRealm(SkysailHashedCredentialsMatcher hashedCredetialsMatcher,
            AuthorizationService authorizationService) {

        this.authenticationSerivce = authorizationService;
        setCredentialsMatcher(hashedCredetialsMatcher);
        setAuthenticationCachingEnabled(true);
        Cache<Object, AuthenticationInfo> authenticationCache = new MapCache<>("credentialsCache",
                new ConcurrentHashMap<Object, AuthenticationInfo>());
        setAuthenticationCache(authenticationCache);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        SkysailUser user = getUser(upToken.getUsername());

        if (user != null) {
            // TODO
            // if (user.isLocked()) {
            // throw new LockedAccountException("Account [" + account +
            // "] is locked.");
            // }
            // if (user.isCredentialsExpired()) {
            // String msg = "The credentials for account [" + account +
            // "] are expired";
            // throw new ExpiredCredentialsException(msg);
            // }
        }

        return new SkysailAuthenticationInfo(user);

    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = getUsername(principals);
        SkysailUser user = getUser(username);

        return new SkysailAuthorizationInfo(user);
    }

    protected String getUsername(PrincipalCollection principals) {
        return getAvailablePrincipal(principals).toString();
    }

    private SkysailUser getUser(String username) {
        if (usernamesAndPasswords.get(username) == null) {
            return null;
        }
        return new SkysailUser(username, usernamesAndPasswords.get(username));
    }

    @Override
    protected void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

}
