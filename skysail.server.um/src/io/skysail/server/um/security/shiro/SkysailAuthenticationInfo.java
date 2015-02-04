package io.skysail.server.um.security.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.subject.PrincipalCollection;

public class SkysailAuthenticationInfo implements AuthenticationInfo {

    private static final long serialVersionUID = 1703161025341435510L;

    private SimpleAuthenticationInfo simpleAuthenticationInfo;

    public SkysailAuthenticationInfo(String username, String password) {
        simpleAuthenticationInfo = new SimpleAuthenticationInfo(username, password.toCharArray(), "internalRealm");
    }

    @Override
    public PrincipalCollection getPrincipals() {
        return simpleAuthenticationInfo.getPrincipals();
    }

    @Override
    public Object getCredentials() {
        return simpleAuthenticationInfo.getCredentials();
    }
}
