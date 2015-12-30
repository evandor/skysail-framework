package io.skysail.server.app.um.db.domain;

import javax.validation.constraints.*;

import io.skysail.domain.html.*;
import lombok.*;

@Getter
@Setter
@ToString
public class Permission extends AbstractUmEntity {

    @Field(inputType = InputType.TEXT)
    @Size(min = 2)
    @NotNull
    private String name;

    @Field(inputType = InputType.TEXTAREA)
    private String description;

}
