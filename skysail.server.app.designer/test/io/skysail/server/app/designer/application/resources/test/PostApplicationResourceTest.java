package io.skysail.server.app.designer.application.resources.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.api.responses.*;
import io.skysail.server.app.designer.application.Application;

import org.junit.Ignore;
import org.junit.Test;
import org.restlet.data.*;
import org.restlet.engine.resource.VariantInfo;

@Ignore
public class PostApplicationResourceTest extends ApplicationResourceTest {



    @Test
    public void empty_form_data_yields_validation_failure() {
        ConstraintViolationsResponse<?> post = (ConstraintViolationsResponse<?>) postApplicationResource.post(form, HTML_VARIANT);
        assertValidationFailure(postApplicationResource, post);
    }

    @Test
    public void empty_json_data_yields_validation_failure() {
        ConstraintViolationsResponse<?> post = (ConstraintViolationsResponse<?>) postApplicationResource.post(new Application(), JSON_VARIANT);
        assertValidationFailure(postApplicationResource, post);
    }

    @Test
    public void valid_form_data_yields_new_entity() {
        form.add("name", "list1");
        SkysailResponse<Application> result = postApplicationResource.post(form, HTML_VARIANT);
        assertListResult(postApplicationResource, result, "list1");
    }

    @Test
    public void valid_json_data_yields_new_entity() {
        SkysailResponse<Application> result = postApplicationResource.post(new Application("jsonList1"), JSON_VARIANT);
        assertThat(responses.get(postApplicationResource.getClass().getName()).getStatus(),is(equalTo(Status.SUCCESS_CREATED)));
        assertListResult(postApplicationResource, result, "jsonList1");
    }

    @Test
    public void two_entries_with_same_name_yields_failure() {
        form.add("name", "list2");
        postApplicationResource.post(form, new VariantInfo(MediaType.TEXT_HTML));
        ConstraintViolationsResponse<?> post = (ConstraintViolationsResponse<?>) postApplicationResource.post(form, HTML_VARIANT);
        assertValidationFailure(postApplicationResource, post);
    }


//    @Test
//    public void empty_form_yields_validation_failure() {
//        ConstraintViolationsResponse<?> post = (ConstraintViolationsResponse<?>) postApplicationResource.post(form, new VariantInfo(MediaType.TEXT_HTML));
//        assertThat(responses.get(resource.getClass().getName()).getStatus(),is(equalTo(Status.CLIENT_ERROR_BAD_REQUEST)));
//        assertThat(responses.get(resource.getClass().getName()).getHeaders().getFirst("X-Status-Reason").getValue(),is(equalTo("Validation failed")));
//        assertThat(post.getViolations().size(),is(4));
//    }
//
//    @Test
////    @Ignore // TODO
//    public void valid_data_yields_new_entity() {
//        form.add("name", "application1");
//        form.add("path", "../");
//        form.add("packageName", "io.skysail.app.test");
//        form.add("projectName", "testproj");
//        resource.post(form, new VariantInfo(MediaType.TEXT_HTML));
//        assertThat(responses.get(resource.getClass().getName()).getStatus(),is(equalTo(Status.SUCCESS_CREATED)));
//    }



}
