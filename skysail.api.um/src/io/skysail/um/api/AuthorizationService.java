package io.skysail.um.api;

import java.util.Set;

import aQute.bnd.annotation.ProviderType;

/**
 * Implementors of this interface are able to determine a set of roles for a
 * given username, e.g. by looking up the user associated with the username in a
 * database and returning the set of assigned roles for that user.
 * 
 * Based on the assigned roles the framework will decide if a users request to a
 * given resource should be authorized.
 *
 */
@ProviderType
public interface AuthorizationService {

    /**
     * get Roles for username.
     * 
     * @param username
     *            the username
     * @return the roles
     */
    Set<String> getRolesFor(String username);

}
