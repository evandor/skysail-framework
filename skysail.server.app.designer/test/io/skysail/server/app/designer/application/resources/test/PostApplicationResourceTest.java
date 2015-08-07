package io.skysail.server.app.designer.application.resources.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Map;

import io.skysail.api.responses.ConstraintViolationsResponse;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.designer.application.Application;

import org.junit.Test;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.engine.resource.VariantInfo;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PostApplicationResourceTest extends AbstractApplicationResourceTest {

    ObjectMapper mapper = new ObjectMapper();

    @Test
    public void empty_form_data_yields_validation_failure() {
        ConstraintViolationsResponse<?> post = (ConstraintViolationsResponse<?>) postApplicationResource.post(form,
                HTML_VARIANT);
        assertValidationFailure(postApplicationResource, post);
    }

    @Test
    public void empty_json_data_yields_validation_failure() {
        ConstraintViolationsResponse<?> post = (ConstraintViolationsResponse<?>) postApplicationResource.post(
                new Application(), JSON_VARIANT);
        assertValidationFailure(postApplicationResource, post);
    }

    @Test
    public void valid_form_data_yields_new_entity() {
        form.add("name", "TestApp");
        form.add("projectName", "TestProject");
        form.add("packageName", "io.skysail.testpackage");
        form.add("path", "../");

        SkysailResponse<Application> result = postApplicationResource.post(form, HTML_VARIANT);
        assertListResult(postApplicationResource, result, Application.builder().name("TestApp").projectName("TestProject").packageName("io.skysail.testpackage").path("../").build());
    }

    @Test
    public void valid_json_data_yields_new_entity() {
        Application app = createValidApplication();
        SkysailResponse<Application> result = postApplicationResource.post(app, JSON_VARIANT);
        assertThat(responses.get(postApplicationResource.getClass().getName()).getStatus(),
                is(equalTo(Status.SUCCESS_CREATED)));
        assertListResult(postApplicationResource, result, app);
    }

    @Test
    public void two_entries_with_same_name_yields_failure() {
        form.add("name", "list2");
        postApplicationResource.post(form, new VariantInfo(MediaType.TEXT_HTML));
        ConstraintViolationsResponse<?> post = (ConstraintViolationsResponse<?>) postApplicationResource.post(form,
                HTML_VARIANT);
        assertValidationFailure(postApplicationResource, post);
    }

}
