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
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.*;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ServerResource;
import org.restlet.routing.Filter;

import io.skysail.server.ext.oauth2.configuration.OAuth2Config;
import io.skysail.server.ext.oauth2.configuration.OAuth2Configurations;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(immediate = true, service = OAuth2Proxy.class)
@NoArgsConstructor
public class OAuth2Proxy extends Filter {

    private final static List<CacheDirective> no = new ArrayList<CacheDirective>();

    private OAuthProviderType providerType;

    @org.osgi.service.component.annotations.Reference
    private OAuth2Configurations configurations;

    private OAuth2Config oAuth2Config;

    private OAuth2Proxy(OAuth2Config oAuth2Config, OAuthProviderType providerType,
            Class<? extends ServerResource> cls) {
        this.oAuth2Config = oAuth2Config;
        this.providerType = providerType;
        setNext(cls);
    }

    public OAuth2Proxy createProxy(OAuthProviderType providerType, String configurationName,
            Class<? extends ServerResource> cls) {
        this.providerType = providerType;
        oAuth2Config = configurations.get(configurationName);
        return new OAuth2Proxy(oAuth2Config, providerType, cls);
    }

    @Activate
    public void activate() {

    }

    @Deactivate
    public void dectivate() {
        oAuth2Config = null;
    }

    @Override
    protected int beforeHandle(Request request, Response response) {
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
                    .setClientId(oAuth2Config.getClientId()).setRedirectURI(oAuth2Config.getRedirectUrl())
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
        OAuthClientRequest request = OAuthClientRequest.tokenProvider(providerType)
                .setGrantType(GrantType.AUTHORIZATION_CODE).setClientId(oAuth2Config.getClientId())
                .setClientSecret(oAuth2Config.getClientSecret()).setRedirectURI(oAuth2Config.getRedirectUrl())
                .setCode(code).buildQueryMessage();

        // create OAuth client that uses custom http client under the hood
        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());

        // Facebook is not fully compatible with OAuth 2.0 draft 10, access
        // token response is
        // application/x-www-form-urlencoded, not json encoded so we use
        // dedicated response class for that
        // Custom response classes are an easy way to deal with oauth providers
        // that introduce modifications to
        // OAuth 2.0 specification

        if (OAuthProviderType.FACEBOOK.equals(providerType)) {
            GitHubTokenResponse oAuthResponse = oAuthClient.accessToken(request, GitHubTokenResponse.class);
            return new Token(oAuthResponse.getAccessToken(), oAuthResponse.getExpiresIn());
        } else {
            GitHubTokenResponse oAuthResponse = oAuthClient.accessToken(request, GitHubTokenResponse.class);
            return new Token(oAuthResponse.getAccessToken(), oAuthResponse.getExpiresIn());
        }
    }

}
