package io.skysail.server.um.security.shiro;

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

public class SkysailAuthorizingRealm extends AuthorizingRealm {

    private ShiroServices shiroServices;

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    public SkysailAuthorizingRealm(SkysailHashedCredentialsMatcherOld hashedCredetialsMatcher, ShiroServices shiroServices) {
        this.shiroServices = shiroServices;
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
            // TODO lock account, ...
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

        return new SkysailAuthenticationInfo(user.getUsername(), user.getPassword());

    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = getUsername(principals);
        SkysailUser user = getUser(username);

        return new SkysailAuthorizationInfo(user.getUsername());
    }

    protected String getUsername(PrincipalCollection principals) {
        return getAvailablePrincipal(principals).toString();
    }

    // TODO code duplication with UserRepository
    private SkysailUser getUser(String username) {
        String sql = "SELECT from SkysailUser WHERE username = :username";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("username", username);
        return null;// shiroServices.getDbService().findOne(sql, SkysailUser.class, params);
    }

    @Override
    protected void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

}
