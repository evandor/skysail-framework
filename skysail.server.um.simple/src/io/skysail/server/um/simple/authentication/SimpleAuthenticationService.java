package io.skysail.server.um.simple.authentication;

import io.skysail.api.um.AuthenticationService;

import org.restlet.Context;
import org.restlet.security.Authenticator;

public class SimpleAuthenticationService implements AuthenticationService {

    @Override
    public Authenticator getAuthenticator(Context context) {
        // https://github.com/qwerky/DataVault/blob/master/src/qwerky/tools/datavault/DataVault.java
        return new SkysailCookieAuthenticator(context, "SKYSAIL_SHIRO_DB_REALM", "thisHasToBecomeM".getBytes());
    }

    @Override
    public void clearCache(String username) {
    }

}
