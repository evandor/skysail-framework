package io.skysail.um.api;

import aQute.bnd.annotation.ProviderType;

@ProviderType
public interface UserManagementProvider {

    AuthenticationService getAuthenticationService();

    AuthorizationService getAuthorizationService();

}
