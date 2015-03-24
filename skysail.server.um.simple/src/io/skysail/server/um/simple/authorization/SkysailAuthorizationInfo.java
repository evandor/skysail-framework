package io.skysail.server.um.simple.authorization;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;

import de.twenty11.skysail.server.um.domain.SkysailUser;

public class SkysailAuthorizationInfo implements AuthorizationInfo {

    private static final long serialVersionUID = -8205213326342471105L;

    private SimpleAuthorizationInfo authInfo;

    public SkysailAuthorizationInfo(SkysailUser user) {
        authInfo = new SimpleAuthorizationInfo();
        Set<String> roles = new HashSet<String>();
        user.getRoles().stream().forEach(r -> roles.add(r.getName()));
        authInfo.setRoles(roles);
    }

    @Override
    public Collection<String> getStringPermissions() {
        return authInfo.getStringPermissions();
    }

    @Override
    public Collection<String> getRoles() {
        return authInfo.getRoles();
    }

    @Override
    public Collection<Permission> getObjectPermissions() {
        return authInfo.getObjectPermissions();
    }

}
