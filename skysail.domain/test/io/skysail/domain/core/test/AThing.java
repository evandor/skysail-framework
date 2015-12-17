package io.skysail.domain.core.test;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.Field;
import lombok.*;

@Getter
@Setter
public class AThing implements Identifiable {

    private String id;

    @Field
    private String stringField;
}