package io.skysail.server.app.todos.todos.resources.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import io.skysail.api.responses.ConstraintViolationsResponse;
import io.skysail.server.app.todos.todos.Todo;
import io.skysail.server.app.todos.todos.resources.PostTodoResource;
import io.skysail.server.app.todos.todos.test.TodoAppTest;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PostTodoResourceTest extends TodoAppTest {

    @InjectMocks
    private PostTodoResource resource;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        // CrmRepository.getInstance().setDbService(dbService);
        resource.init(null, request, response);
    }

    @Test
    public void creates_entity_template() throws Exception {
        resource.init(null, null, null);
        Todo todo = resource.createEntityTemplate();
        assertThat(todo.getTitle(), is(nullValue()));
    }

    @Test
    @Ignore
    public void missing_title_in_html_post_yields_failed_validation() {
        Object result = resource.post(form);

        assertValidationFailed(400, "Validation failed");
        assertOneConstraintViolation((ConstraintViolationsResponse<?>) result, "name", "may not be null");
    }

    @Test
    public void posting_minimal_html_form_creates_new_entity() {
        form.add("title", "mytitle");
        form.add("due", "");
        Todo post = (Todo) resource.post(form);
        assertThat(post.getTitle(), is(equalTo("mytitle")));
    }

    @Test
    public void posting_minimal_entity_creates_new_entity() {
        Todo todo = new Todo();
        todo.setTitle("mytitle");
        Todo post = (Todo) resource.post(todo);
        assertThat(post.getTitle(), is(equalTo("mytitle")));
    }

}
