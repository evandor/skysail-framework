package io.skysail.server.db.it;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.db.it.one2many.todo.*;
import io.skysail.server.db.it.one2many.todo.resources.*;

import java.util.*;
import java.util.stream.Collectors;

import org.junit.*;

public class OneToManyDbTests extends DbIntegrationTests {

    private PostTodoResource postTodoResource;
    private TodoResource todoResource;
    private TodosResource todosResource;
    private PutTodoResource putTodoResource;

    @BeforeClass
    public static void beforeClass() {
        skysailApplication = TodoApplication.getInstance();
    }

    @Before
    public void setUp() throws Exception {
        postTodoResource = new PostTodoResource();
        todoResource = new TodoResource();
        todosResource = new TodosResource();
        putTodoResource = new PutTodoResource();

        setupEntityResource(postTodoResource);
        setupEntityResource(todoResource);
        setupEntityResource(todosResource);
        setupEntityResource(putTodoResource);

        request.clearAttributes();
    }

    @Test
    public void creates_a_new_simple_entity() {
        Todo simpleEntity = createSimpleEntity();
        SkysailResponse<Todo> post = postTodoResource.post(simpleEntity, HTML_VARIANT);
        assertThat(post.getEntity(),is(simpleEntity));
        assertThat(post.getEntity().getCreated(),is(notNullValue()));
        assertThat(post.getEntity().getModified(),is(nullValue()));
    }

    @Test
    public void retrieves_a_Todo_by_id() {
        Todo Todo = createSimpleEntity();
        String newTodoId = postTodoResource.post(Todo, HTML_VARIANT).getEntity().getId();
        request.addAttribute("id", newTodoId);
        Todo TodoFromDb = todoResource.getEntity();
        assertThat(TodoFromDb.getTitle(),is(Todo.getTitle()));
    }

    @Test
    public void retrieves_the_list_of_Todos() {
        Todo newTodo = createSimpleEntity();
        postTodoResource.post(newTodo, HTML_VARIANT);
        postTodoResource.post(newTodo, HTML_VARIANT);
        List<Todo> Todos = todosResource.getEntities(HTML_VARIANT).getEntity();
        assertThat(Todos.stream().filter(Todo -> Todo.getTitle().equals(newTodo.getTitle())).collect(Collectors.toList()).size(),is(2));
    }

    @Test
    public void Todo_can_be_updated() {
        Todo Todo = createSimpleEntity();
        String TodoId = postTodoResource.post(Todo, HTML_VARIANT).getEntity().getId();
        request.addAttribute("id", TodoId);
        Todo.setTitle("changed");
        putTodoResource.putEntity(Todo, HTML_VARIANT).getEntity();

        Todo TodoFromDb = todoResource.getEntity();
        assertThat(TodoFromDb.getModified(),is(notNullValue()));
        assertThat(TodoFromDb.getTitle(),is("changed"));
    }

    private Todo createSimpleEntity() {
        String name = Long.toString(new Date().getTime());
        Todo simpleEntity = new Todo(name);
        return simpleEntity;
    }

}
