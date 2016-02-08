package io.skysail.server.ext.oauth2.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class OAuth2Config {
    private final @NonNull String name, clientId, clientSecret, redirectUrl;
}