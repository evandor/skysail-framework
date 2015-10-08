package io.skysail.server.um.db.authorization;

import io.skysail.api.um.UserManagementProvider;

import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

public class DbAuthorizingRealm extends AuthorizingRealm {

    private UserManagementProvider simpleUserManagementProvider;

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    public DbAuthorizingRealm(SimpleCredentialsMatcher hashedCredetialsMatcher,
            UserManagementProvider simpleUserManagementProvider) {
        this.simpleUserManagementProvider = simpleUserManagementProvider;
//        setCredentialsMatcher(hashedCredetialsMatcher);
//        setAuthenticationCachingEnabled(true);
//        Cache<Object, AuthenticationInfo> authenticationCache = new MapCache<>("credentialsCache",
//                new ConcurrentHashMap<Object, AuthenticationInfo>());
//        setAuthenticationCache(authenticationCache);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String username = upToken.getUsername();
        if (username == null) {
            throw new IllegalArgumentException("Null usernames are not allowed by this realm.");
        }
        return null;//new SkysailAuthenticationInfo(user);

    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = getUsername(principals);
       // SkysailUser user = getUserByPrincipal(username);

        return null;//new SkysailAuthorizationInfo(user);
    }

    protected String getUsername(PrincipalCollection principals) {
        return getAvailablePrincipal(principals).toString();
    }

//    private SkysailUser getUser(String username) {
//        return simpleUserManagementProvider.getByUsername(username);
//    }
//
//    private SkysailUser getUserByPrincipal(String principal) {
//        return simpleUserManagementProvider.getByPrincipal(principal);
//    }
//
    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

}
