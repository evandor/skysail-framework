package io.skysail.server.app.todos.todos.resources.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.todos.*;
import io.skysail.server.app.todos.lists.UniquePerOwnerValidator;
import io.skysail.server.app.todos.repo.TodosRepository;
import io.skysail.server.app.todos.todos.Todo;
import io.skysail.server.app.todos.todos.resources.*;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.testsupport.ResourceTestBase;

import java.util.HashMap;

import org.apache.shiro.subject.SimplePrincipalMap;
import org.junit.Before;
import org.mockito.*;
import org.restlet.data.Status;

public abstract class TodoResourceTest extends ResourceTestBase {

    @Spy
    protected PostTodoResource postListresource;

    @Spy
    protected PutTodoResource putListResource;

    @Spy
    protected TodosResource listsResource;

    @Spy
    protected TodoResource listResource;

    protected TodosRepository repo;

    @Before
    public void setUp() throws Exception {
        super.setUpFixture();
        super.setUpApplication(Mockito.mock(TodoApplication.class));
        super.setUpResource(listResource);
        super.setUpResource(listsResource);
        super.setUpResource(putListResource);
        super.setUpResource(postListresource);
        setUpRepository(new TodosRepository());
        setUpSubject("admin");

        new UniquePerOwnerValidator().setDbService(testDb);
    }

    protected void assertResult(SkysailServerResource<?> resource, SkysailResponse<TodoList> result, String name) {
        TodoList entity = result.getEntity();
        assertThat(responses.get(resource.getClass().getName()).getStatus(),is(equalTo(Status.SUCCESS_CREATED)));
        assertThat(entity.getName(),is(equalTo(name)));
        assertThat(entity.getCreated(),is(not(nullValue())));
        assertThat(entity.getModified(),is(nullValue()));
        assertThat(entity.getOwner(),is("admin"));
        //assertThat(result.getEntity().getTodosCount(),is(0));
    }

    public void setUpRepository(TodosRepository todosRepository) {
        repo = todosRepository;
        repo.setDbService(testDb);
        repo.activate();
        ((TodoApplication)application).setRepository(repo);
        Mockito.when(((TodoApplication)application).getRepository()).thenReturn(repo);

    }

    public void setUpSubject(String owner) {
        Mockito.when(subjectUnderTest.getPrincipal()).thenReturn(owner);
        Mockito.when(subjectUnderTest.getPrincipals()).thenReturn(new SimplePrincipalMap(new HashMap<>()));
        setSubject(subjectUnderTest);
    }

    protected Todo createTodo() {
        Todo aList = new Todo();
//        aList.setName("list_" + randomString());
        SkysailResponse<Todo> post = postListresource.post(aList,JSON_VARIANT);
        getAttributes().clear();

        return post.getEntity();
    }


}
