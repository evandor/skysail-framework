package io.skysail.server.db.it.one2many.todo;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.*;
import io.skysail.server.db.*;
import io.skysail.server.db.it.one2many.comment.Comment;

import java.util.Date;

import javax.persistence.Id;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * A todo entity with title, without validation, and a collection
 * of comments.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
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

    @Field(inputType = InputType.HIDDEN)
    @JsonDeserialize(using = CustomOutEdgesDeserializer.class)
    private OutEdges<Comment> comments = new OutEdges<>();

}
