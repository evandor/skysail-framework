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
        
        
//        
    }
}
