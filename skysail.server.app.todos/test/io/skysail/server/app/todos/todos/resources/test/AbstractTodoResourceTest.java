package io.skysail.server.app.todos.todos.resources.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.todos.*;
import io.skysail.server.app.todos.lists.*;
import io.skysail.server.app.todos.repo.TodosRepository;
import io.skysail.server.app.todos.todos.*;
import io.skysail.server.app.todos.todos.resources.*;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.testsupport.ResourceTestBase;

import java.util.HashMap;

import org.apache.shiro.subject.SimplePrincipalMap;
import org.junit.Before;
import org.mockito.*;
import org.restlet.data.Status;

public abstract class AbstractTodoResourceTest extends ResourceTestBase {

    @Spy
    protected PostTodoResource postTodoResource;

    @Spy
    protected PutTodoResource putTodoResource;

    @Spy
    protected TodosResource todosResource;

    @Spy
    protected TodoResource todoResource;

    @Spy
    protected PostListResource postListResource;

    @Spy
    protected TodoApplication application;

    protected TodosRepository repo;

    @Before
    public void setUp() throws Exception {
        super.setUpFixture();
        super.setUpApplication(application);
        super.setUpResource(todoResource);
        super.setUpResource(todosResource);
        super.setUpResource(putTodoResource);
        super.setUpResource(postTodoResource);
        super.setUpResource(postListResource);
        setUpRepository(new TodosRepository());
        setUpSubject("admin");

        new UniquePerOwnerValidator().setDbService(testDb);
        new ValidListIdValidator().setDbService(testDb);
    }

    protected void assertTodoResult(SkysailServerResource<?> resource, SkysailResponse<Todo> result, String name) {
        Todo entity = result.getEntity();
        assertThat(responses.get(resource.getClass().getName()).getStatus(),is(equalTo(Status.SUCCESS_CREATED)));
        assertThat(entity.getTitle(),is(equalTo(name)));
        assertThat(entity.getCreated(),is(not(nullValue())));
        assertThat(entity.getModified(),is(nullValue()));
        assertThat(entity.getOwner(),is("admin"));
    }

    public void setUpRepository(TodosRepository todosRepository) {
        repo = todosRepository;
        repo.setDbService(testDb);
        repo.activate();
        ((TodoApplication) application).setRepository(repo);
        Mockito.when(((TodoApplication) application).getRepository()).thenReturn(repo);

    }

    public void setUpSubject(String owner) {
        Mockito.when(subjectUnderTest.getPrincipal()).thenReturn(owner);
        Mockito.when(subjectUnderTest.getPrincipals()).thenReturn(new SimplePrincipalMap(new HashMap<>()));
        setSubject(subjectUnderTest);
    }

    protected TodoList createList() {
        TodoList aList = new TodoList();
        aList.setName("list_" + randomString());
        SkysailResponse<TodoList> post = postListResource.post(aList, JSON_VARIANT);
        getAttributes().clear();

        return post.getEntity();
    }

    protected Todo createTodo(TodoList list) {
        Todo aTodo = new Todo();
        aTodo.setTitle("todo_" + randomString());
        aTodo.setParent(list.getId());
        SkysailResponse<Todo> post = postTodoResource.post(aTodo, JSON_VARIANT);
        getAttributes().clear();

        return post.getEntity();
    }

    protected void init(SkysailServerResource<?> resource) {
        resource.init(null, request, responses.get(resource.getClass().getName()));
    }

    protected void setAttributes(String name, String id) {
        getAttributes().clear();
        getAttributes().put(name, id);
    }


}
