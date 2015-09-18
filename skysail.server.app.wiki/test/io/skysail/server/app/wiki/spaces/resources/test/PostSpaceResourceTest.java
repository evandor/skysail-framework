package io.skysail.server.app.wiki.spaces.resources.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.api.responses.*;
import io.skysail.server.app.wiki.spaces.Space;

import org.junit.*;
import org.restlet.data.*;
import org.restlet.engine.resource.VariantInfo;

@Ignore
public class PostSpaceResourceTest extends AbstractSpaceResourceTest {

    @Test
    public void empty_form_data_yields_validation_failure() {
        SkysailResponse<Space> posted = postSpaceResource.post(form, HTML_VARIANT);
        assertSingleValidationFailure(postSpaceResource, posted,  "name", "may not be null");
    }

    @Test
    public void empty_json_data_yields_validation_failure() {
        SkysailResponse<Space> posted =  postSpaceResource.post(new Space(), JSON_VARIANT);
        assertSingleValidationFailure(postSpaceResource, posted, "name", "may not be null");
    }

    @Test
    public void valid_form_data_yields_new_entity() {
        form.add("name", "list1");
        SkysailResponse<Space> result = postSpaceResource.post(form, HTML_VARIANT);
        assertSpaceResult(postSpaceResource, result, "list1");
    }

    @Test
    public void valid_json_data_yields_new_entity() {
        SkysailResponse<Space> result = postSpaceResource.post(new Space("jsonSpace1"), JSON_VARIANT);
        assertThat(responses.get(postSpaceResource.getClass().getName()).getStatus(),is(equalTo(Status.SUCCESS_CREATED)));
        assertSpaceResult(postSpaceResource, result, "jsonSpace1");
        assertThat(result.getEntity().getOwner(), is("admin"));
    }

    @Test
    public void two_entries_with_same_name_yields_failure() {
        form.add("name", "same_name");
        postSpaceResource.post(form, new VariantInfo(MediaType.TEXT_HTML));
        ConstraintViolationsResponse<?> post = (ConstraintViolationsResponse<?>) postSpaceResource.post(form, HTML_VARIANT);
        assertSingleValidationFailure(postSpaceResource, post,  "", "name already exists");
    }

}
