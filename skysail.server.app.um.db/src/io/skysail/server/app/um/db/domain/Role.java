package io.skysail.server.app.um.db.domain;

import io.skysail.api.forms.*;
import io.skysail.domain.Identifiable;

import javax.persistence.Id;
import javax.validation.constraints.*;

import lombok.*;

@Getter
@Setter
@ToString
public class Role implements Identifiable {

    @Id
    private String id;

    @Field(inputType = InputType.TEXT)
    @Size(min = 2)
    @NotNull
    private String name;

}
