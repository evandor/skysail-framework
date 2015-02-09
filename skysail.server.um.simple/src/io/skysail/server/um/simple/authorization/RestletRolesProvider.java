package io.skysail.server.um.simple.authorization;

import java.util.List;

import org.restlet.security.Role;

public interface RestletRolesProvider {

    Role getRole(String name);

    List<Role> getRoles();

}
