package io.skysail.server.app.todos.lists.test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.server.app.todos.TodoList;

import java.util.List;

import org.junit.Test;
import org.restlet.data.Status;

public class ListsResourceTest extends TodoListResourceTest {

    @Test
    public void todoList_contains_created_todo_list() {
        TodoList aList = createList();

        listsResource.init(null, request, responses.get(listsResource.getClass().getName()));

        List<TodoList> get = listsResource.getEntity();

        assertThat(responses.get(listsResource.getClass().getName()).getStatus(),is(equalTo(Status.SUCCESS_OK)));
        assertThat(get.size(),is(greaterThanOrEqualTo(1)));

        TodoList theList = get.stream().filter(list -> list.getName().equals(aList.getName())).findFirst().orElseThrow(IllegalStateException::new);

        assertThat(theList.getName(),is(equalTo(aList.getName())));
        assertThat(theList.getCreated(), is(notNullValue()));
    }

}
