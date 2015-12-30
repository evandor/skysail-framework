package io.skysail.server.app.um.db.domain;

import javax.persistence.Id;
import javax.validation.constraints.*;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.*;
import lombok.*;

/**
 * Not used yet
 *
 */
@Getter
@Setter
@ToString
public class Group implements Identifiable {

    @Id
    private String id;

    @Field(inputType = InputType.TEXT)
    @Size(min = 2)
    @NotNull
    private String name;

    @Field(inputType = InputType.TEXTAREA)
    private String description;

}
