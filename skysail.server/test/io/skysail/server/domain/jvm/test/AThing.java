package io.skysail.server.domain.jvm.test;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import lombok.*;

@Getter
@Setter
public class AThing implements Identifiable {

    private String id;

    @Field
    private String stringField;
}