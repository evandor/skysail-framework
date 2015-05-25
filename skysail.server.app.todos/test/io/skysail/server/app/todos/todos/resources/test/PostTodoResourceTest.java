package io.skysail.server.app.todos.todos.resources.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.api.responses.ConstraintViolationsResponse;
import io.skysail.server.app.todos.*;
import io.skysail.server.app.todos.lists.UniquePerOwnerValidator;
import io.skysail.server.app.todos.repo.TodosRepository;
import io.skysail.server.app.todos.todos.*;
import io.skysail.server.app.todos.todos.resources.PostTodoResource;
import io.skysail.server.testsupport.PostResourceTest;

import java.util.HashMap;

import org.apache.shiro.subject.SimplePrincipalMap;
import org.junit.*;
import org.mockito.*;
import org.restlet.data.*;
import org.restlet.engine.resource.VariantInfo;

public class PostTodoResourceTest extends PostResourceTest {

    @Spy
    private PostTodoResource resource;

    @Before
    public void setUp() throws Exception {
        super.setUp(Mockito.mock(TodoApplication.class), resource);

        TodosRepository repo = new TodosRepository();
        repo.setDbService(testDb);
        repo.activate();
        ((TodoApplication) application).setRepository(repo);
        Mockito.when(((TodoApplication) application).getRepository()).thenReturn(repo);

        Mockito.when(subjectUnderTest.getPrincipal()).thenReturn("admin");
        Mockito.when(subjectUnderTest.getPrincipals()).thenReturn(new SimplePrincipalMap(new HashMap<>()));
        setSubject(subjectUnderTest);

        new UniquePerOwnerValidator().setDbService(testDb);
        new ValidListIdValidator().setDbService(testDb);

    }

    @Test
    @Ignore
    public void empty_html_form_yields_validation_failure() {
        form.add("title", "title_" + randomString());
        ConstraintViolationsResponse<?> post = (ConstraintViolationsResponse<?>) resource.post(form, new VariantInfo(
                MediaType.TEXT_HTML));
        assertValidationFailure(post, "list", "may not be null");
    }

    @Test
    public void wrong_list_yields_validation_failure() { 
        form.add("title", "title_" + randomString());
        form.add("list", "list_" + randomString());
        ConstraintViolationsResponse<?> post = (ConstraintViolationsResponse<?>) resource.post(form, new VariantInfo(MediaType.TEXT_HTML));
        assertValidationFailure(post, "list", "This list does not exist or has another owner");
    }

    @Test
    public void valid_data_yields_new_entity() {
        
        TodoList entity = new TodoList();
        entity.setName("list_" + randomString());
        entity.setOwner("admin");
        String id = TodosRepository.add(entity).toString();
        
        form.add("title", "title_" + randomString());
        form.add("list", id);
        form.add("due", "22/12/2099");
        Todo post = (Todo) resource.post(form, new VariantInfo(MediaType.TEXT_HTML));
        assertThat(response.getStatus(), is(equalTo(Status.SUCCESS_CREATED)));
    }

}
