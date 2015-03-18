package io.skysail.server.um.simple.authorization;

import java.util.List;

import org.restlet.security.Role;

import aQute.bnd.annotation.ProviderType;

@ProviderType
public interface RestletRolesProvider {

    Role getRole(String name);

    List<Role> getRoles();

}
