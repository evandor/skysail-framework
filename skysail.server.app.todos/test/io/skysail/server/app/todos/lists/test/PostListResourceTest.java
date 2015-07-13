package io.skysail.server.app.todos.lists.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.api.responses.ConstraintViolationsResponse;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.todos.TodoApplication;
import io.skysail.server.app.todos.TodoList;
import io.skysail.server.app.todos.lists.UniquePerOwnerValidator;
import io.skysail.server.app.todos.repo.TodosRepository;

import java.util.HashMap;

import org.apache.shiro.subject.SimplePrincipalMap;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.engine.resource.VariantInfo;

public class PostListResourceTest extends ListResourceTest {

    @Before
    public void setUp() throws Exception {
        super.setUp(Mockito.mock(TodoApplication.class));
        super.setUp(postListresource);


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
    public void empty_form_data_yields_validation_failure() {
        ConstraintViolationsResponse<?> post = (ConstraintViolationsResponse<?>) postListresource.post(form, HTML_VARIANT);
        assertValidationFailure(post,  "name", "may not be null");
    }

    @Test
    public void empty_json_data_yields_validation_failure() {
        ConstraintViolationsResponse<?> post = (ConstraintViolationsResponse<?>) postListresource.post(new TodoList(), JSON_VARIANT);
        assertValidationFailure(post, "name", "may not be null");
    }

    @Test
    public void valid_form_data_yields_new_entity() {
        form.add("name", "list1");
        SkysailResponse<TodoList> result = postListresource.post(form, HTML_VARIANT);
        assertResult(result, "list1");
    }

    @Test
    public void valid_json_data_yields_new_entity() {
        SkysailResponse<TodoList> result = postListresource.post(new TodoList("jsonList1"), JSON_VARIANT);
        assertThat(response.getStatus(),is(equalTo(Status.SUCCESS_CREATED)));
        assertResult(result, "jsonList1");
    }
    
    @Test
    public void two_entries_with_same_name_yields_failure() {
        form.add("name", "list2");
        postListresource.post(form, new VariantInfo(MediaType.TEXT_HTML));
        ConstraintViolationsResponse<?> post = (ConstraintViolationsResponse<?>) postListresource.post(form, HTML_VARIANT);
        assertValidationFailure(post,  "", "name already exists");
    }

}

