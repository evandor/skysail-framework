package io.skysail.um.api;

import org.restlet.Request;
import org.restlet.Response;

import aQute.bnd.annotation.ProviderType;

@ProviderType
public interface AuthenticationService {

    boolean authenticate(Request request, Response response);

    void logout();

    // SkysailUser getCurrentUser();

    /**
     * Validate if provided authToken has the proper credentials for the given
     * user.
     * 
     */
    // boolean validateAuthToken(UsernamePasswordToken authToken);

    void clearCache(String username);

}
