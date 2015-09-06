package io.skysail.server.app.todos.lists.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.todos.*;
import io.skysail.server.app.todos.repo.TodosRepository;

import java.util.List;

import org.junit.Test;

import com.tinkerpop.blueprints.impls.orient.OrientVertex;

public class PutListResourceTest extends AbstractListResourceTest {

    @Test
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
        SkysailResponse<TodoList> skysailResponse = putListResource.put(updatedList, JSON_VARIANT);

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

        List<OrientVertex> vertexById2 = (List<OrientVertex>) new TodosRepository().getVertexById(TodoList.class, aList.getId());
        OrientVertex vertexById = vertexById2.get(0);

        assertThat(vertexById.getProperty("modified"), is(not(nullValue())));
        assertThat(vertexById.getProperty("created"), is(not(nullValue())));
        assertThat(vertexById.getProperty("name"), is(equalTo("updated_list")));
        assertThat(vertexById.getProperty("desc"), is(equalTo("description")));
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

        List<OrientVertex> vertexById2 = (List<OrientVertex>) new TodosRepository().getVertexById(TodoList.class, aList.getId());
        OrientVertex vertexById = vertexById2.get(0);

        assertThat(vertexById.getProperty("name"), is(equalTo("updated_list_with_default_flag_becomes_default")));
        assertThat(vertexById.getProperty("defaultList"), is(true));

    }
}
