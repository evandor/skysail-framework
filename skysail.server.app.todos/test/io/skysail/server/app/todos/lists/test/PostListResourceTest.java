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

public class PostListResourceTest extends PostResourceTest {

    @Spy
    private PostListResource resource;

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
    public void empty_html_form_yields_validation_failure() {
        ConstraintViolationsResponse<?> post = (ConstraintViolationsResponse<?>) resource.post(form, new VariantInfo(MediaType.TEXT_HTML));
        assertValidationFailure(post,  "name", "may not be null");
    }

    @Test
    public void empty_json_request_yields_validation_failure() {
        ConstraintViolationsResponse<?> post = (ConstraintViolationsResponse<?>) resource.post(new TodoList(), new VariantInfo(MediaType.APPLICATION_JSON));
        assertValidationFailure(post, "name", "may not be null");
    }

    @Test
    public void valid_form_data_yields_new_entity() {
        form.add("name", "list1");
        resource.post(form, new VariantInfo(MediaType.TEXT_HTML));
        assertThat(response.getStatus(),is(equalTo(Status.SUCCESS_CREATED)));
    }

    @Test
    public void valid_data_yields_new_entity() {
        TodoList newTodoList = new TodoList("jsonList1");
        resource.post(newTodoList, new VariantInfo(MediaType.APPLICATION_JSON));
        assertThat(response.getStatus(),is(equalTo(Status.SUCCESS_CREATED)));
    }

    @Test
    public void two_entries_with_same_name_yields_failure() {
        form.add("name", "list2");
        resource.post(form, new VariantInfo(MediaType.TEXT_HTML));
        ConstraintViolationsResponse<?> post = (ConstraintViolationsResponse<?>) resource.post(form, new VariantInfo(MediaType.TEXT_HTML));
        assertValidationFailure(post,  "", "name already exists");
    }


}
