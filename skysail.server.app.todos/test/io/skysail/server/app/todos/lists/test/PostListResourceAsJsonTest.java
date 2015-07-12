package io.skysail.server.app.todos.lists.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.api.responses.ConstraintViolationsResponse;
import io.skysail.server.app.todos.TodoList;

import org.junit.Test;
import org.restlet.data.*;
import org.restlet.engine.resource.VariantInfo;
import org.restlet.representation.Variant;

public class PostListResourceAsJsonTest extends PostListResourceTest {

    private Variant jsonVariant = new VariantInfo(MediaType.APPLICATION_JSON);

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
