package io.skysail.server.app.todos.lists.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.api.responses.*;
import io.skysail.server.app.todos.TodoList;

import org.junit.Test;
import org.restlet.data.*;
import org.restlet.engine.resource.VariantInfo;

public class PostListResourceTest extends TodoListResourceTest {

    @Test
    public void empty_form_data_yields_validation_failure() {
        ConstraintViolationsResponse<?> post = (ConstraintViolationsResponse<?>) postListresource.post(form, HTML_VARIANT);
        assertSingleValidationFailure(postListresource, post,  "name", "may not be null");
    }

    @Test
    public void empty_json_data_yields_validation_failure() {
        ConstraintViolationsResponse<?> post = (ConstraintViolationsResponse<?>) postListresource.post(new TodoList(), JSON_VARIANT);
        assertSingleValidationFailure(postListresource, post, "name", "may not be null");
    }

    @Test
    public void valid_form_data_yields_new_entity() {
        form.add("name", "list1");
        SkysailResponse<TodoList> result = postListresource.post(form, HTML_VARIANT);
        assertListResult(postListresource, result, "list1");
    }

    @Test
    public void valid_json_data_yields_new_entity() {
        SkysailResponse<TodoList> result = postListresource.post(new TodoList("jsonList1"), JSON_VARIANT);
        assertThat(responses.get(postListresource.getClass().getName()).getStatus(),is(equalTo(Status.SUCCESS_CREATED)));
        assertListResult(postListresource, result, "jsonList1");
    }

    @Test
    public void two_entries_with_same_name_yields_failure() {
        form.add("name", "list2");
        postListresource.post(form, new VariantInfo(MediaType.TEXT_HTML));
        ConstraintViolationsResponse<?> post = (ConstraintViolationsResponse<?>) postListresource.post(form, HTML_VARIANT);
        assertSingleValidationFailure(postListresource, post,  "", "name already exists");
    }

}

