package io.skysail.api.um;

import org.restlet.Context;
import org.restlet.security.Authenticator;

import aQute.bnd.annotation.ProviderType;

@ProviderType
public interface AuthenticationService {

    Authenticator getAuthenticator(Context context);

    // Needed?
    void clearCache(String username);

}
