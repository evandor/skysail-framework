package io.skysail.api.um;

import aQute.bnd.annotation.ProviderType;

@ProviderType
public interface UserManagementProvider {

    AuthenticationService getAuthenticationService();

    AuthorizationService getAuthorizationService();

}
