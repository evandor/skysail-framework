package io.skysail.server.app.designer.restclient;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;

import javax.persistence.Id;

import lombok.*;

@Getter
@Setter
public class AuthenticationType implements Identifiable  {

    @Id
    private String id;

    @Field
    private String name;

    private Authentication type;
}
