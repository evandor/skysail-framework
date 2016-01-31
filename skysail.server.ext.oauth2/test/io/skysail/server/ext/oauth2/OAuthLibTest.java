package io.skysail.server.ext.oauth2;

import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.common.OAuthProviderType;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.junit.Test;

public class OAuthLibTest {

    @Test
    public void testName() throws OAuthSystemException {
        OAuthClientRequest request = OAuthClientRequest
                .authorizationProvider(OAuthProviderType.FACEBOOK)
                .setClientId("1481445908830165")
                .setRedirectURI("http://localhost:2016/Twitter/v1/facebook")
                .buildQueryMessage();
        String locationUri = request.getLocationUri();
        System.out.println(locationUri);
        
        
//        OAuthClientRequest request = OAuthClientRequest
//                .tokenProvider(OAuthProviderType.FACEBOOK)
//                .setGrantType(GrantType.AUTHORIZATION_CODE)
//                .setClientId("1481445908830165")
//                .setClientSecret("d2991f2ae851ef74d65134e596574f88")
//                .setRedirectURI("http://localhost:2016/Twitter/v1/facebook")
//                .setCode(code)
//                .buildQueryMessage();
// 
//            //create OAuth client that uses custom http client under the hood
//            OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
// 
//            //Facebook is not fully compatible with OAuth 2.0 draft 10, access token response is
//            //application/x-www-form-urlencoded, not json encoded so we use dedicated response class for that
//            //Custom response classes are an easy way to deal with oauth providers that introduce modifications to
//            //OAuth 2.0 specification
//            GitHubTokenResponse oAuthResponse = oAuthClient.accessToken(request, GitHubTokenResponse.class);
// 
//            String accessToken = oAuthResponse.getAccessToken();
//            String expiresIn = oAuthResponse.getExpiresIn();
    }
}
