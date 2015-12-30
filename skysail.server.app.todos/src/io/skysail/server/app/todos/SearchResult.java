package io.skysail.server.app.todos;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import io.skysail.server.app.todos.todos.Todo;

import javax.persistence.Id;

import lombok.Getter;

@Getter
public class SearchResult implements Identifiable {

    @Id
    private String id;

    @Field
    private String title;

    public SearchResult(Todo h) {
        this.title = h.getTitle();
        this.id = h.getId();
    }

    @Override
    public void setId(String id) {

    }

}
