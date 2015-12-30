package io.skysail.domain.core.test;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.*;
import lombok.*;

@Getter
@Setter
public class AThing implements Identifiable {

    private String id;

    @Field(inputType = InputType.TEXT)
    private String stringField;
}