package io.skysail.server.db.it.one2many.todo;

import java.util.*;

import javax.persistence.Id;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.*;
import io.skysail.server.db.it.one2many.comment.Comment;
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

    //    @FieldModel(inputType = InputType.MULTISELECT, repository = RoleRepository.class, selectionProvider = RolesSelectionProvider.class)
    @Field(inputType = InputType.HIDDEN)
    private List<Comment> comments = new ArrayList<>();

}
