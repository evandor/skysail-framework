package io.skysail.server.app.designer.restclient;

import io.skysail.api.forms.*;
import io.skysail.server.codegen.annotations.GenerateResources;
import io.skysail.server.forms.ListView;
import lombok.*;

@Getter
@Setter
@GenerateResources(application = "io.skysail.server.app.designer.restclient.RestclientApplication")
public class OAuth2 extends AuthenticationType {

    @Field
    private String clientId;

    @Field(inputType = InputType.PASSWORD)
    @ListView(hide = true)
    private String clientSecret;

    @Field(inputType = InputType.URL)
    private String redirectUri, authorizationUri, tokenUri;

    public OAuth2() {
        setType(Authentication.OAUTH2);
    }
}
