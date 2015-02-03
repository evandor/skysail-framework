package io.skysail.api.um;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.security.Authenticator;

import aQute.bnd.annotation.ProviderType;

@ProviderType
public interface AuthenticationService {

    boolean authenticate(Request request, Response response);

    void logout();

    User getCurrentUser();

    /**
     * Validate if provided authToken has the proper credentials for the given
     * user.
     * 
     */
    // boolean validateAuthToken(UsernamePasswordToken authToken);

    void clearCache(String username);

    Authenticator getAuthenticator(Context context);

}
