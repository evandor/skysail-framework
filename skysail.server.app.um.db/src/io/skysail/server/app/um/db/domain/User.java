package io.skysail.server.app.um.db.domain;

import java.util.*;

import javax.persistence.Id;
import javax.validation.constraints.*;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.*;
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

    @Field(inputType = InputType.MULTISELECT, selectionProvider = RolesSelectionProvider.class)
    private List<Role> roles = new ArrayList<>();

   // @Field(inputType = InputType.MULTISELECT, repository = GroupRepository.class, selectionProvider = GroupsSelectionProvider.class)
   // private List<Group> groups = new ArrayList<>();

}
