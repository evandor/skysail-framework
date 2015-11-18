package io.skysail.server.db.test;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.*;

import java.util.*;

import javax.persistence.Id;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A todo entity with title, without validation, and a collection
 * of comments.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class TodoWithList implements Identifiable {

    public TodoWithList(String title) {
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
    private List<Comment> comments = new ArrayList<>();

}
