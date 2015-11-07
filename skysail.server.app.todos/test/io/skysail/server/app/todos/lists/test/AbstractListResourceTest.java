package io.skysail.server.app.todos.lists.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.todos.*;
import io.skysail.server.app.todos.lists.*;
import io.skysail.server.app.todos.repo.*;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.testsupport.ResourceTestBase;

import java.util.HashMap;

import org.apache.shiro.subject.SimplePrincipalMap;
import org.junit.Before;
import org.mockito.*;
import org.restlet.Context;

public abstract class AbstractListResourceTest extends ResourceTestBase {

    @Spy
    public PostListResource postListresource;

    @Spy
    public PutListResource putListResource;

    @Spy
    public ListsResource listsResource;

    @Spy
    public ListResource listResource;

    @Spy
    private TodoApplication application;

    protected TodosRepository todoRepo;
    protected ListsRepository listRepo;

    @Before
    public void setUp() throws Exception {
        super.setUpFixture();

        setUpTodosRepository(new TodosRepository());
        setUpListRepository(new ListsRepository());

        Context context = super.setUpApplication(application);
        super.setUpResource(listResource,context);
        super.setUpResource(listsResource,context);
        super.setUpResource(putListResource,context);
        super.setUpResource(postListresource,context);
        setUpSubject("admin");

        new UniquePerOwnerValidator().setDbService(testDb);
    }

    public void assertListResult(SkysailServerResource<?> resource, SkysailResponse<TodoList> result, String name) {
        TodoList entity = result.getEntity();
        //assertThat(responses.get(resource.getClass().getName()).getStatus(),is(equalTo(Status.REDIRECTION_SEE_OTHER)));
        assertThat(entity.getName(),is(equalTo(name)));
        assertThat(entity.getCreated(),is(not(nullValue())));
        assertThat(entity.getModified(),is(nullValue()));
        assertThat(entity.getOwner(),is("admin"));
    }

    public void setUpTodosRepository(TodosRepository repo) {
        this.todoRepo = repo;
        repo.setDbService(testDb);
        repo.activate();
        ((TodoApplication)application).setTodoRepository(repo);
        Mockito.when(((TodoApplication)application).getTodosRepo()).thenReturn(repo);

    }

    public void setUpListRepository(ListsRepository repo) {
        this.listRepo = repo;
        listRepo.setDbService(testDb);
        listRepo.activate();
        ((TodoApplication)application).setTodoListRepository(repo);
        Mockito.when(((TodoApplication)application).getListRepo()).thenReturn(listRepo);

    }

    public void setUpSubject(String owner) {
        Mockito.when(subjectUnderTest.getPrincipal()).thenReturn(owner);
        Mockito.when(subjectUnderTest.getPrincipals()).thenReturn(new SimplePrincipalMap(new HashMap<>()));
        setSubject(subjectUnderTest);
    }

    protected TodoList createList() {
        TodoList aList = new TodoList();
        aList.setName("list_" + randomString());
        SkysailResponse<TodoList> post = postListresource.post(aList,JSON_VARIANT);
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
