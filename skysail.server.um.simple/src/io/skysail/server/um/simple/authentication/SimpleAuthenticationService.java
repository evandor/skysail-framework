package io.skysail.server.um.simple.authentication;

import io.skysail.api.um.AuthenticationService;
import io.skysail.api.um.User;
import io.skysail.server.um.simple.authorization.SimpleAuthorizingRealm;
import lombok.extern.slf4j.Slf4j;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.security.Authenticator;

@Slf4j
public class SimpleAuthenticationService implements AuthenticationService {

    private SimpleAuthorizingRealm authorizingRealm;

    @Override
    public boolean authenticate(Request request, Response response) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void logout() {
        // TODO Auto-generated method stub

    }

    @Override
    public User getCurrentUser() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void clearCache(String username) {
        // TODO Auto-generated method stub

    }

    @Override
    public Authenticator getAuthenticator(Context context) {
        // https://github.com/qwerky/DataVault/blob/master/src/qwerky/tools/datavault/DataVault.java
        return new SimpleDelegationAuthenticator(context, "SKYSAIL_SHIRO_DB_REALM", "thisHasToBecomeM".getBytes());
    }

}
