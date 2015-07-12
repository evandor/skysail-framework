package io.skysail.server.app.todos.lists.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.api.responses.ConstraintViolationsResponse;

import org.junit.Test;
import org.restlet.data.*;
import org.restlet.engine.resource.VariantInfo;
import org.restlet.representation.Variant;

public class PostListResourceAsHtmlTest extends PostListResourceTest {

    private Variant textHtmlVariant = new VariantInfo(MediaType.TEXT_HTML);

    @Test
    public void empty_html_form_yields_validation_failure() {
        ConstraintViolationsResponse<?> post = (ConstraintViolationsResponse<?>) resource.post(form, textHtmlVariant);
        assertValidationFailure(post,  "name", "may not be null");
    }

    @Test
    public void valid_form_data_yields_new_entity() {
        form.add("name", "list1");
        resource.post(form, textHtmlVariant);
        assertThat(response.getStatus(),is(equalTo(Status.SUCCESS_CREATED)));
    }

    @Test
    public void two_entries_with_same_name_yields_failure() {
        form.add("name", "list2");
        resource.post(form, new VariantInfo(MediaType.TEXT_HTML));
        ConstraintViolationsResponse<?> post = (ConstraintViolationsResponse<?>) resource.post(form, textHtmlVariant);
        assertValidationFailure(post,  "", "name already exists");
    }


}
