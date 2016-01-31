package io.skysail.server.ext.oauth2;

import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "OAuth Config", pid = "oAuth2")
public @interface OAuth2Config {

    String clientId();

    String clientSecret();

}