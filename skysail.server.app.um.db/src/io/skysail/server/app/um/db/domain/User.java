package io.skysail.server.app.um.db.domain;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.*;
import io.skysail.server.app.um.db.repo.RoleRepository;

import java.util.List;

import javax.persistence.Id;
import javax.validation.constraints.*;

import lombok.*;

@Getter
@Setter
@ToString
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

    @Field(inputType = InputType.MULTISELECT, repository = RoleRepository.class, selectionProvider = RolesSelectionProvider.class)
    private List<Role> roles;
}
