package io.skysail.domain.core.test;

import io.skysail.api.forms.*;
import io.skysail.domain.Identifiable;
import lombok.*;

@Getter
@Setter
public class AThing implements Identifiable {

    private String id;

    @Field(inputType = InputType.TEXT)
    private String stringField;
}