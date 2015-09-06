package io.skysail.server.app.todos.lists.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.todos.*;
import io.skysail.server.app.todos.lists.*;
import io.skysail.server.app.todos.repo.TodosRepository;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.testsupport.ResourceTestBase;

import java.util.HashMap;

import org.apache.shiro.subject.SimplePrincipalMap;
import org.junit.Before;
import org.mockito.*;
import org.restlet.data.Status;

public abstract class AbstractListResourceTest extends ResourceTestBase {

    @Spy
    protected PostListResource postListresource;

    @Spy
    protected PutListResource putListResource;

    @Spy
    protected ListsResource listsResource;

    @Spy
    protected ListResource listResource;

    @Spy
    private TodoApplication application;

    protected TodosRepository repo;

    @Before
    public void setUp() throws Exception {
        super.setUpFixture();

        super.setUpApplication(application);//Mockito.mock(TodoApplication.class));
        super.setUpResource(listResource);
        super.setUpResource(listsResource);
        super.setUpResource(putListResource);
        super.setUpResource(postListresource);
        setUpRepository(new TodosRepository());
        setUpSubject("admin");

        new UniquePerOwnerValidator().setDbService(testDb);
    }

    protected void assertListResult(SkysailServerResource<?> resource, SkysailResponse<TodoList> result, String name) {
        TodoList entity = result.getEntity();
        assertThat(responses.get(resource.getClass().getName()).getStatus(),is(equalTo(Status.SUCCESS_CREATED)));
        assertThat(entity.getName(),is(equalTo(name)));
        assertThat(entity.getCreated(),is(not(nullValue())));
        assertThat(entity.getModified(),is(nullValue()));
        assertThat(entity.getOwner(),is("admin"));
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
