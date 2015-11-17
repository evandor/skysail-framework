package io.skysail.server.app.designer.restclient;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.Field;

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
