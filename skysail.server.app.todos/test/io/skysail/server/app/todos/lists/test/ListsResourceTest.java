package io.skysail.server.app.todos.lists.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.restlet.data.Status;

import io.skysail.server.app.todos.TodoList;

@Ignore

public class ListsResourceTest extends AbstractListResourceTest {

    @Test
    public void todoList_contains_created_todo_list() {
        TodoList aList = createList();

        init(listsResource);
        List<TodoList> get = listsResource.getEntity();

        assertThat(responses.get(listsResource.getClass().getName()).getStatus(), is(equalTo(Status.SUCCESS_OK)));
        assertThat(get.size(), is(greaterThanOrEqualTo(1)));

        TodoList theList = get.stream().filter(list -> list.getName().equals(aList.getName())).findFirst()
                .orElseThrow(IllegalStateException::new);

        assertThat(theList.getName(), is(equalTo(aList.getName())));
        assertThat(theList.getCreated(), is(notNullValue()));
    }

}
