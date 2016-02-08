package io.skysail.server.app.todos.lists.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Ignore;
import org.junit.Test;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.todos.TodoApplication;
import io.skysail.server.app.todos.TodoList;

@Ignore
public class PutListResourceTest extends AbstractListResourceTest {

    @Test
    @Ignore
    public void empty_form_data_yields_validation_failure() {
        TodoList aList = createList();

        form.add("name", "");
        form.add("id", aList.getId());
        setAttributes(TodoApplication.LIST_ID, aList.getId());
        init(putListResource);

        SkysailResponse<TodoList> skysailResponse = putListResource.put(form, HTML_VARIANT);

        assertSingleValidationFailure(putListResource, skysailResponse,  "name", "size must be between");
    }

    @Test
    public void empty_json_data_yields_validation_failure() {

        init(putListResource);

        TodoList updatedList = new TodoList();
        SkysailResponse<TodoList> skysailResponse = putListResource.putEntity(updatedList, JSON_VARIANT);

        assertSingleValidationFailure(putListResource, skysailResponse,  "name", "may not be null");
    }

    @Test
    public void list_can_be_updated() {
        TodoList aList = createList();

        form.add("name", "updated_list");
        form.add("desc", "description");
        putListResource.getRequestAttributes().put(TodoApplication.LIST_ID, aList.getId());
        setAttributes(TodoApplication.LIST_ID, aList.getId());
        init(putListResource);

        putListResource.put(form, HTML_VARIANT);

        TodoList findOne = listRepo.findOne(aList.getId());

        assertThat(findOne.getModified(), is(not(nullValue())));
        assertThat(findOne.getCreated(), is(not(nullValue())));
        assertThat(findOne.getName(), is(equalTo("updated_list")));
        assertThat(findOne.getDesc(), is(equalTo("description")));
    }

    @Test
    public void updated_list_with_default_flag_becomes_default() {
        TodoList aList = createList();

        form.add("name", "updated_list_with_default_flag_becomes_default");
        form.add("defaultList", "on");

        putListResource.getRequestAttributes().put(TodoApplication.LIST_ID, aList.getId());
        setAttributes(TodoApplication.LIST_ID, aList.getId());
        init(putListResource);

        putListResource.put(form, HTML_VARIANT);

        TodoList findOne = listRepo.findOne(aList.getId());

        assertThat(findOne.getName(), is(equalTo("updated_list_with_default_flag_becomes_default")));
        assertThat(findOne.isDefaultList(), is(true));

    }
}
