package io.skysail.api.um;

import java.util.List;

import org.restlet.security.Role;

import aQute.bnd.annotation.ProviderType;

@ProviderType
public interface RestletRolesProvider {

    Role getRole(String name);

    List<Role> getRoles();

}
