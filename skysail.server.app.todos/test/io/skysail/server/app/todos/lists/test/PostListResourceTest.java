package io.skysail.server.app.todos.lists.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.api.responses.*;
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
    public void empty_form_yields_validation_failure() {
        
        Object post = submit(form);
        
        assertThat(post, instanceOf(ConstraintViolationsResponse.class));
        assertThat(response.getStatus(),is(equalTo(Status.CLIENT_ERROR_BAD_REQUEST)));
        assertThat(response.getHeaders().getFirst("X-Status-Reason").getValue(),is(equalTo("Validation failed")));
        assertThat(((ConstraintViolationsResponse<?>)post).getViolations().size(),is(1));
        ConstraintViolationDetails violation = ((ConstraintViolationsResponse<?>)post).getViolations().iterator().next();
        assertThat(((ConstraintViolationDetails)violation).getMessage(),is(containsString("may not be null")));
    }
    
    @Test
    public void valid_data_yields_new_entity() {
        form.add("name", "list1");
        Object post = submit(form);
        
        assertThat(post, instanceOf(TodoList.class));
        assertThat(response.getStatus(),is(equalTo(Status.SUCCESS_CREATED)));
    }

    @Test
    public void two_entries_with_same_name_yields_failure() {
        form.add("name", "list2");
        submit(form);
        Object post = submit(form);
        
        assertThat(post, instanceOf(ConstraintViolationsResponse.class));
        assertThat(response.getStatus(),is(equalTo(Status.CLIENT_ERROR_BAD_REQUEST)));
        assertThat(response.getHeaders().getFirst("X-Status-Reason").getValue(),is(equalTo("Validation failed")));
        assertThat(((ConstraintViolationsResponse<?>)post).getViolations().size(),is(1));
        ConstraintViolationDetails violation = ((ConstraintViolationsResponse<?>)post).getViolations().iterator().next();
        assertThat(((ConstraintViolationDetails)violation).getMessage(),is(containsString("name already exists")));

    }

    private Object submit(Form theForm) {
        return resource.post(theForm, new VariantInfo(MediaType.TEXT_HTML));
    }
}

