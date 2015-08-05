package io.skysail.server.app.todos.lists.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.todos.*;

import org.junit.*;
import org.restlet.data.Status;

public class ListResourceTest extends TodoListResourceTest {

    @Test
    public void gets_list_representation() {
        TodoList aList = createList();

        getAttributes().put(TodoApplication.LIST_ID, aList.getId());
        init(listResource);

        SkysailResponse<TodoList> get = listResource.getEntity2(HTML_VARIANT);

        assertThat(responses.get(listResource.getClass().getName()).getStatus(), is(equalTo(Status.SUCCESS_OK)));
        assertThat(get.getEntity().getName(), is(equalTo(aList.getName())));
        assertThat(get.getEntity().getCreated(), is(notNullValue()));
    }

    @Test
    @Ignore
    public void deletes_list_resource_if_empty() {
        TodoList aList = createList();

        setAttributes(TodoApplication.LIST_ID, aList.getId());
        init(listResource);

        listResource.deleteEntity(HTML_VARIANT);
        assertThat(responses.get(listResource.getClass().getName()).getStatus(), is(equalTo(Status.SUCCESS_OK)));

        Object byId = repo.getById(TodoList.class, aList.getId());
        assertThat(byId, is(nullValue()));
    }

    @Test
    public void does_not_delete_list_with_todo() {

    }
}
