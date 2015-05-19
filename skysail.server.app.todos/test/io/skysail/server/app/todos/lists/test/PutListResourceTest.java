package io.skysail.server.app.todos.lists.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.server.app.todos.*;
import io.skysail.server.app.todos.lists.*;
import io.skysail.server.app.todos.repo.TodosRepository;
import io.skysail.server.testsupport.PutResourceTest;

import java.util.HashMap;

import org.apache.shiro.subject.SimplePrincipalMap;
import org.junit.*;
import org.mockito.*;
import org.restlet.data.*;
import org.restlet.engine.resource.VariantInfo;

public class PutListResourceTest extends PutResourceTest {

    @Spy
    private PutListResource resource;
    
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
    public void empty_form_yields_validation_failure() {
        
        TodoList entity = new TodoList();
        entity.setName("list3");
        String id = TodosRepository.add(entity).toString();
        
        form.add("name", "list3a");
        form.add("id", id);
        resource.getRequestAttributes().put(TodoApplication.LIST_ID, id);
        resource.init(null, request, response);
        
        resource.put(form, new VariantInfo(MediaType.TEXT_HTML));
        
        TodoList entityFromDb = new TodosRepository().getById(TodoList.class, id);
        
        assertThat(response.getStatus(),is(equalTo(Status.SUCCESS_OK)));
        assertThat(entityFromDb.getModified(), is(notNullValue()));
        assertThat(entityFromDb.getName(), is(equalTo("list3a")));
//        assertThat(response.getHeaders().getFirst("X-Status-Reason").getValue(),is(equalTo("Validation failed")));
//        assertThat(((ConstraintViolationsResponse<?>)put).getViolations().size(),is(1));
//        ConstraintViolationDetails violation = ((ConstraintViolationsResponse<?>)put).getViolations().iterator().next();
//        assertThat(((ConstraintViolationDetails)violation).getMessage(),is(containsString("may not be null")));
    }
    
}
