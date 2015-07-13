package io.skysail.server.app.todos.lists.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.server.app.todos.TodoApplication;
import io.skysail.server.app.todos.TodoList;
import io.skysail.server.app.todos.lists.ListsResource;
import io.skysail.server.app.todos.lists.UniquePerOwnerValidator;
import io.skysail.server.app.todos.repo.TodosRepository;
import io.skysail.server.testsupport.ResourceTestBase;

import java.util.HashMap;
import java.util.List;

import org.apache.shiro.subject.SimplePrincipalMap;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.restlet.data.Status;

public class ListsResourceTest extends ResourceTestBase {

    @Spy
    private ListsResource resource;
    
    private TodosRepository repo;

    @Before
    public void setUp() throws Exception {
        super.setUp(Mockito.mock(TodoApplication.class));
        super.setUp(resource);

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
