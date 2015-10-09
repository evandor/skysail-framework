package io.skysail.server.app.um.db.domain;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.*;

import java.util.List;

import javax.persistence.Id;
import javax.validation.constraints.*;

import lombok.*;

@Getter
@Setter
public class User implements Identifiable {

    @Id
    private String id;

    @Field(inputType = InputType.EMAIL)
    private String username;

    @Field(inputType = InputType.TEXT)
    private String firstname;

    @Field(inputType = InputType.TEXT)
    @Size(min = 2)
    @NotNull
    private String lastname;

    private List<Role> roles;
}
