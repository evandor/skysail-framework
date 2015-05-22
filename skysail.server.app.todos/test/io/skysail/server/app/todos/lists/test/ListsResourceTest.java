package io.skysail.server.app.todos.lists.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.server.app.todos.*;
import io.skysail.server.app.todos.lists.*;
import io.skysail.server.app.todos.repo.TodosRepository;
import io.skysail.server.testsupport.ResourceTestBase;

import java.util.*;

import org.apache.shiro.subject.SimplePrincipalMap;
import org.junit.*;
import org.mockito.*;
import org.restlet.data.Status;

public class ListsResourceTest extends ResourceTestBase {

    @Spy
    private ListsResource resource;
    
    private TodosRepository repo;

    @Before
    public void setUp() throws Exception {
        super.setUp(Mockito.mock(TodoApplication.class), resource);

        repo = new TodosRepository();
        repo.setDbService(testDb);
        repo.activate();
        ((TodoApplication)application).setRepository(repo);
        Mockito.when(((TodoApplication)application).getRepository()).thenReturn(repo);
        
        Mockito.when(subjectUnderTest.getPrincipal()).thenReturn("admin");
        Mockito.when(subjectUnderTest.getPrincipals()).thenReturn(new SimplePrincipalMap(new HashMap<>()));
        setSubject(subjectUnderTest);
        
        new UniquePerOwnerValidator().setDbService(testDb);
    }

    @Test
    @Ignore
    public void empty_form_yields_validation_failure() {
        
        TodoList entity = new TodoList();
        entity.setName("list3");
        entity.setOwner("admin");
        TodosRepository.add(entity).toString();
        
        resource.init(null, request, response);
        
        List<TodoList> get = resource.getEntity();
        
        assertThat(response.getStatus(),is(equalTo(Status.SUCCESS_OK)));
        assertThat(get.size(),is(1));
    }

}
