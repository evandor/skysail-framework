package io.skysail.server.ext.oauth2;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.GitHubTokenResponse;
import org.apache.oltu.oauth2.common.OAuthProviderType;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.*;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.routing.Filter;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OAuth2Proxy extends Filter {

    private final static List<CacheDirective> no = new ArrayList<CacheDirective>();

    private OAuthProviderType providerType;

    private String clientId;
    private String clientSecret;

    @Setter
    private String redirectUri;

    public OAuth2Proxy(OAuthProviderType providerType, String clientId, String clientSecret) {
        this.providerType = providerType;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    @Override
    protected int beforeHandle(Request request, Response response) {
        // Sets the no-store Cache-Control header
        request.setCacheDirectives(no);

        Form params = new Form(request.getOriginalRef().getQuery());
        log.info("Incomming request query = " + params);

        try {
            // // Check if error is available.
            //// String error = params.getFirstValue(ERROR);
            //// if (error != null && !error.isEmpty()) {
            //// validateState(request, params); // CSRF protection
            //// return sendErrorPage(response,
            //// OAuthException.toOAuthException(params));
            //// }
            //// // Check if code is available.
            String code = params.getFirstValue("code");
            if (StringUtils.isNotEmpty(code)) {
                // validateState(request, params); // CSRF protection
                Token token = go(code);
                request.getAttributes().put(Token.class.getName(), token);
                return CONTINUE;
            }
        } catch (Exception ex) {
            return sendErrorPage(response, ex);
        }

        // Redirect to authorization uri
        // OAuthParameters authRequest = createAuthorizationRequest();
        // authRequest.state(setupState(response)); // CSRF protection

        OAuthClientRequest oAuthClientRequest;
        try {
            oAuthClientRequest = OAuthClientRequest.authorizationProvider(providerType)
                    .setClientId(clientId).setRedirectURI("http://localhost:2016/OAuth2/v1/facebook")
                    .buildQueryMessage();
            String locationUri = oAuthClientRequest.getLocationUri();

            Reference redirRef = new Reference(locationUri); // authRequest.toReference(getAuthorizationURI());
            log.info("Redirecting to : " + redirRef.toUri());
            response.setCacheDirectives(no);
            response.redirectTemporary(redirRef);
            log.info("After Redirecting to : " + redirRef.toUri());
        } catch (OAuthSystemException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return STOP;
    }
    
    private int sendErrorPage(Response response, Exception ex) {
        response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST, ex.getMessage());
        response.setEntity(getErrorPage(ex));
        return STOP;
    }

    private Representation getErrorPage(Exception ex) {
        // Failed in initial auth resource request
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body><pre>");
        sb.append("General error detected.\n");
        sb.append("Error : ").append(ex.getMessage());

        sb.append("</pre></body></html>");

        return new StringRepresentation(sb.toString(), MediaType.TEXT_HTML);
    }

    private Token go(String code) throws OAuthSystemException, OAuthProblemException {
        OAuthClientRequest request = OAuthClientRequest.tokenProvider(OAuthProviderType.FACEBOOK)
                .setGrantType(GrantType.AUTHORIZATION_CODE).setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectURI(redirectUri).setCode(code).buildQueryMessage();

        // create OAuth client that uses custom http client under the hood
        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());

        // Facebook is not fully compatible with OAuth 2.0 draft 10, access
        // token response is
        // application/x-www-form-urlencoded, not json encoded so we use
        // dedicated response class for that
        // Custom response classes are an easy way to deal with oauth providers
        // that introduce modifications to
        // OAuth 2.0 specification
        GitHubTokenResponse oAuthResponse = oAuthClient.accessToken(request, GitHubTokenResponse.class);

        return new Token(oAuthResponse.getAccessToken(), oAuthResponse.getExpiresIn());
    }

}
