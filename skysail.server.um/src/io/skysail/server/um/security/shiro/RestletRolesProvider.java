package io.skysail.server.um.security.shiro;

import java.util.List;

import org.restlet.security.Role;

public interface RestletRolesProvider {

    Role getRole(String name);

    List<Role> getRoles();

}
