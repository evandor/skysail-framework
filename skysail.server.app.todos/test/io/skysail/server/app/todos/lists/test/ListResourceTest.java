package io.skysail.server.app.todos.lists.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Ignore;
import org.junit.Test;
import org.restlet.data.Status;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.todos.TodoApplication;
import io.skysail.server.app.todos.TodoList;

@Ignore
public class ListResourceTest extends AbstractListResourceTest {

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
    public void deletes_list_resource_if_empty() {
        TodoList aList = createList();

        setAttributes(TodoApplication.LIST_ID, aList.getId());
        init(listResource);

        listResource.deleteEntity(HTML_VARIANT);
        assertThat(responses.get(listResource.getClass().getName()).getStatus(), is(equalTo(Status.REDIRECTION_SEE_OTHER)));

        Object byId = todoRepo.findOne(aList.getId());
        assertThat(byId, is(nullValue()));
    }

    @Test
    public void does_not_delete_list_with_todo() {

    }
}
