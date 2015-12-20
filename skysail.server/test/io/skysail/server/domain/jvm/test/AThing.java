package io.skysail.server.domain.jvm.test;

import io.skysail.api.forms.Field;
import io.skysail.domain.Identifiable;
import lombok.*;

@Getter
@Setter
public class AThing implements Identifiable {

    private String id;

    @Field
    private String stringField;
}