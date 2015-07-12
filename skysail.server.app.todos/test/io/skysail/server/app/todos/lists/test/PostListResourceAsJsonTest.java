package io.skysail.server.app.todos.lists.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.api.responses.ConstraintViolationsResponse;
import io.skysail.server.app.todos.*;
import io.skysail.server.app.todos.lists.*;
import io.skysail.server.app.todos.repo.TodosRepository;
import io.skysail.server.testsupport.PostResourceTest;

import java.util.HashMap;

import org.apache.shiro.subject.SimplePrincipalMap;
import org.junit.*;
import org.mockito.*;
import org.restlet.data.*;
import org.restlet.engine.resource.VariantInfo;
import org.restlet.representation.Variant;

public class PostListResourceAsJsonTest extends PostResourceTest {

    @Spy
    private PostListResource resource;
    
    private Variant jsonVariant = new VariantInfo(MediaType.APPLICATION_JSON);

    @Before
    public void setUp() throws Exception {
        super.setUp(Mockito.mock(TodoApplication.class), resource);

        TodosRepository repo = new TodosRepository();
        repo.setDbService(testDb);
        repo.activate();
        ((TodoApplication)application).setRepository(repo);
        
        Mockito.when(subjectUnderTest.getPrincipal()).thenReturn("admin");
        Mockito.when(subjectUnderTest.getPrincipals()).thenReturn(new SimplePrincipalMap(new HashMap<>()));
        setSubject(subjectUnderTest);
        
        new UniquePerOwnerValidator().setDbService(testDb);
    }

    @Test
    public void empty_json_request_yields_validation_failure() {
        ConstraintViolationsResponse<?> post = (ConstraintViolationsResponse<?>) resource.post(new TodoList(), jsonVariant);
        assertValidationFailure(post, "name", "may not be null");
    }

    @Test
    public void valid_data_yields_new_entity() {
        TodoList newTodoList = new TodoList("jsonList1");
        resource.post(newTodoList, jsonVariant);
        assertThat(response.getStatus(),is(equalTo(Status.SUCCESS_CREATED)));
    }


}
