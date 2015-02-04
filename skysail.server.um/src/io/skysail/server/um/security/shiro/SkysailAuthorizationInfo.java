package io.skysail.server.um.security.shiro;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;

public class SkysailAuthorizationInfo implements AuthorizationInfo {

    private SimpleAuthorizationInfo authInfo;

    public SkysailAuthorizationInfo(String username) {
        authInfo = new SimpleAuthorizationInfo();
        // authInfo.setRoles(user.getRoles().stream().map(role ->
        // role.getName()).collect(Collectors.toSet()));
        Set<String> roles = new HashSet<String>();
        roles.add("admin");
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
