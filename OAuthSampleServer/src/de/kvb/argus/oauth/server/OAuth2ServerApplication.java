package de.kvb.argus.oauth.server;

import org.restlet.*;
import org.restlet.data.*;
import org.restlet.ext.oauth.*;
import org.restlet.ext.oauth.internal.*;
import org.restlet.resource.Directory;
import org.restlet.routing.Router;
import org.restlet.security.ChallengeAuthenticator;

public class OAuth2ServerApplication extends Application {

    public static void main(String[] args) throws Exception {
        Server oauthServer = new Server(Protocol.HTTP, 8111);
        oauthServer.setNext(new OAuth2ServerApplication());
        try {
            oauthServer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public OAuth2ServerApplication() {
    }

    @Override
    public synchronized Restlet createInboundRoot() {
        Router router = new Router(getContext());

        getContext().getAttributes().put(ClientManager.class.getName(), OAuth2Sample.getClientManager());
        getContext().getAttributes().put(TokenManager.class.getName(), OAuth2Sample.getTokenManager());

        // Setup Authorize Endpoint
        router.attach("/authorize", AuthorizationServerResource.class);
        router.attach(HttpOAuthHelper.getAuthPage(getContext()), AuthPageServerResource.class);
        HttpOAuthHelper.setAuthPageTemplate("authorize.html", getContext());
        HttpOAuthHelper.setAuthSkipApproved(true, getContext());
        HttpOAuthHelper.setErrorPageTemplate("error.html", getContext());
        router.attach(HttpOAuthHelper.getLoginPage(getContext()), LoginPageServerResource.class);

        // Setup Token Endpoint
        ChallengeAuthenticator clientAuthenticator = new ChallengeAuthenticator(getContext(),
                ChallengeScheme.HTTP_BASIC, "OAuth2Sample");
        ClientVerifier clientVerifier = new ClientVerifier(getContext());
        clientVerifier.setAcceptBodyMethod(true);
        clientAuthenticator.setVerifier(clientVerifier);
        clientAuthenticator.setNext(AccessTokenServerResource.class);
        router.attach("/token", clientAuthenticator);

        // Setup Token Auth for Resources Server
        router.attach("/token_auth", TokenAuthServerResource.class);

        final Directory resources = new Directory(getContext(), "clap://system/resources");
        router.attach("", resources);

        return router;
    }


}