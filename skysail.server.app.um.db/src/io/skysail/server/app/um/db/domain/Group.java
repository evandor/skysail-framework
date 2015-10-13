package io.skysail.server.app.um.db.domain;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.*;

import javax.persistence.Id;
import javax.validation.constraints.*;

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
