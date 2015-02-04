package de.twenty11.skysail.server.security;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.restlet.Context;
import org.restlet.security.Authenticator;

import de.twenty11.skysail.server.um.domain.SkysailUser;

public interface AuthenticationService {

    Authenticator getAuthenticator(Context context);

    void logout();

    SkysailUser getCurrentUser();

    /**
     * Validate if provided authToken has the proper credentials for the given
     * user.
     * 
     * @param authToken
     */
    boolean validateAuthToken(UsernamePasswordToken authToken);

    void clearCache(String username);

}
