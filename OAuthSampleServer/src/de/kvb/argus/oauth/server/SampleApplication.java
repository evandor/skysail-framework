package de.kvb.argus.oauth.server;

import org.restlet.*;
import org.restlet.data.*;
import org.restlet.ext.oauth.TokenVerifier;
import org.restlet.routing.Router;
import org.restlet.security.ChallengeAuthenticator;

public class SampleApplication extends Application {

    @Override
    public synchronized Restlet createInboundRoot() {
        Router router = new Router(getContext());

        router.attach("/status", StatusServerResource.class);

        /*
         * Since Role#hashCode and Role#equals are not implemented,
         * RoleAuthorizer cannot be used.
         */
        // RoleAuthorizer roleAuthorizer = new RoleAuthorizer();
        // roleAuthorizer.setAuthorizedRoles(Scopes.toRoles("status"));
        // roleAuthorizer.setNext(router);

        ChallengeAuthenticator bearerAuthenticator = new ChallengeAuthenticator(
                getContext(), ChallengeScheme.HTTP_OAUTH_BEARER, "OAuth2Sample");
        bearerAuthenticator.setVerifier(new TokenVerifier(new Reference(
                "riap://component/oauth/token_auth")));
        bearerAuthenticator.setNext(router);

        return bearerAuthenticator;
    }
}
