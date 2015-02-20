package io.skysail.api.um;

import org.restlet.Context;
import org.restlet.security.Authenticator;

import aQute.bnd.annotation.ProviderType;

@ProviderType
public interface AuthenticationService {

    Authenticator getAuthenticator(Context context);

    void updatePassword(User user, String newPassword);

    // Needed?
    void clearCache(String username);

}
