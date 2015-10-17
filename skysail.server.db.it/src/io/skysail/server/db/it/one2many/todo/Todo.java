package io.skysail.server.db.it.one2many.todo;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.Field;

import java.util.Date;

import javax.persistence.Id;

import lombok.*;

/**
 * A todo entity with title, without validation, and a collection
 * of comments.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Todo implements Identifiable {

    public Todo(String title) {
        this.title = title;
    }

    @Id
    private String id;

    @Field
    private String title;

    @Field
    private Date created;

    @Field
    private Date modified;

//    @Field(inputType = InputType.MULTISELECT, repository = RoleRepository.class, selectionProvider = RolesSelectionProvider.class)
//    private List<Role> roles = new ArrayList<>();

}
