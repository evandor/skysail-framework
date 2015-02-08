package io.skysail.server.um.simple.authentication;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.subject.PrincipalCollection;

import de.twenty11.skysail.server.um.domain.SkysailUser;

public class SkysailAuthenticationInfo implements AuthenticationInfo {

    private static final long serialVersionUID = 1703161025341435510L;

    private SimpleAuthenticationInfo simpleAuthenticationInfo;

    public SkysailAuthenticationInfo(SkysailUser user) {
        simpleAuthenticationInfo = new SimpleAuthenticationInfo(user.getUsername(), user.getPassword().toCharArray(),
                "internalRealm");
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
