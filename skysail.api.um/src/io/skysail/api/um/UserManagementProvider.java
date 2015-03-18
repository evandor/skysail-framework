package io.skysail.api.um;

import aQute.bnd.annotation.ProviderType;

/**
 * Authentication and Authorization Services
 *
 */
@ProviderType
public interface UserManagementProvider {

    AuthenticationService getAuthenticationService();

    AuthorizationService getAuthorizationService();

}
