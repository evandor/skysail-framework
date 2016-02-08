package io.skysail.server.ext.oauth2;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Token {

    private final String accessToken;
    private final Long expiresIn;
    

}
