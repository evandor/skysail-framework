package io.skysail.server.app.todos.todos.resources.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.todos.*;
import io.skysail.server.app.todos.todos.Todo;

import org.junit.*;
import org.mockito.Mockito;
import org.restlet.data.*;
import org.restlet.engine.resource.VariantInfo;

public class PutTodoResourceTest extends AbstractTodoResourceTest {

    private TodoList aList;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        aList = createList();
        getAttributes().put(TodoApplication.LIST_ID, aList.getId());
    }

    @Test
    @Ignore
    public void rejects_updating_password_if_old_password_is_null() throws Exception {
        Mockito.when(subjectUnderTest.getPrincipal()).thenReturn("admin");
        setSubject(subjectUnderTest);
        // resource.put(form, null);
        // assertThat(response.getStatus().getCode(), is(400));
    }

    @Test
    public void todo_can_be_updated() {
        Todo todo = createTodo(aList);

        form.add("title", "changed");
        form.add("status", io.skysail.server.app.todos.todos.status.Status.WIP.name());
        form.add("id", todo.getId());
        form.add("parent", aList.getId());
        setAttributes(TodoApplication.LIST_ID, aList.getId());
        setAttributes(TodoApplication.TODO_ID, todo.getId());
        init(putTodoResource);

        SkysailResponse<Todo> response = putTodoResource.put(form, new VariantInfo(MediaType.TEXT_HTML));

        assertThat(responses.get(putTodoResource.getClass().getName()).getStatus(), is(equalTo(Status.SUCCESS_OK)));
       // assertThat(response.getEntity().getModified(), is(notNullValue()));
        assertThat(response.getEntity().getTitle(), is(equalTo("changed")));
        assertThat(response.getEntity().getStatus(), is(equalTo(io.skysail.server.app.todos.todos.status.Status.WIP)));

//        Object byId = application.getRepository().getById(Todo.class, todo.getId());
//
//        //putTodoResource.getRequestAttributes().put(TodoApplication.TODO_ID, todo.getId());
//        setAttributes(TodoApplication.TODO_ID, todo.getId());
//        init(todoResource);
//        Todo entity = todoResource.getEntity();
//
//        List<OrientVertex> vertexById2 = (List<OrientVertex>) new TodosRepository().getVertexById(Todo.class, todo.getId());
//        OrientVertex vertexById = vertexById2.get(0);
//
//        assertThat(vertexById.getProperty("modified"), is(notNullValue()));
//
////        Todo entityFromDb = repo.getById(Todo.class, todo.getId());
////
    }

    @Test
    @Ignore
    public void updating_owner_attribute_is_ignored() {
        Todo todo = createTodo(aList);

        form.add("title", "changed");
        form.add("status", io.skysail.server.app.todos.todos.status.Status.WIP.name());
        form.add("id", todo.getId());
        form.add("parent", aList.getId());
        form.add("owner", "me");
        setAttributes(TodoApplication.LIST_ID, aList.getId());
        setAttributes(TodoApplication.TODO_ID, todo.getId());
        init(putTodoResource);

        SkysailResponse<Todo> response = putTodoResource.put(form, new VariantInfo(MediaType.TEXT_HTML));

        assertThat(responses.get(putTodoResource.getClass().getName()).getStatus(), is(equalTo(Status.SUCCESS_OK)));
        assertThat(response.getEntity().getOwner(), is(equalTo("admin")));
    }

}
