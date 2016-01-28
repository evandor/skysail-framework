package io.skysail.server.app.designer.application.resources.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.engine.resource.VariantInfo;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.skysail.api.responses.ConstraintViolationsResponse;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.testsupport.FormBuilder;

public class PostApplicationResourceTest extends AbstractApplicationResourceTest {

    ObjectMapper mapper = new ObjectMapper();

    @Test
    public void empty_form_data_yields_validation_failure() {
        ConstraintViolationsResponse<?> post = (ConstraintViolationsResponse<?>) postApplicationResource.post(new Form(),
                HTML_VARIANT);
        assertValidationFailure(postApplicationResource, post);
    }

    @Test
    public void empty_json_data_yields_validation_failure() {
        ConstraintViolationsResponse<?> post = (ConstraintViolationsResponse<?>) postApplicationResource.post(
                new DbApplication(), JSON_VARIANT);
        assertValidationFailure(postApplicationResource, post);
    }

    @Test
    public void valid_form_data_yields_new_entity() {
        Form aForm = new FormBuilder().add("name", "TestApp")
            .add("projectName", "TestProject")
            .add("packageName", "io.skysail.testpackage")
            .add("path", "../")
            .build();

        SkysailResponse<DbApplication> result = postApplicationResource.post(aForm, HTML_VARIANT);
        assertListResult(postApplicationResource, result, 
                DbApplication.builder().name("TestApp").projectName("TestProject").packageName("io.skysail.testpackage").path("../").build(),
                Status.REDIRECTION_SEE_OTHER);
    }

    @Test
    public void valid_json_data_yields_new_entity() {
        DbApplication app = createValidApplication();
        SkysailResponse<DbApplication> result = postApplicationResource.post(app, JSON_VARIANT);
        assertThat(responses.get(postApplicationResource.getClass().getName()).getStatus(),
                is(equalTo(Status.SUCCESS_CREATED)));
        assertListResult(postApplicationResource, result, app, Status.SUCCESS_CREATED);
    }

    @Test
    public void two_entries_with_same_name_yields_failure() {
        form.add("name", "list2");
        postApplicationResource.post(form, new VariantInfo(MediaType.TEXT_HTML));
        ConstraintViolationsResponse<?> post = (ConstraintViolationsResponse<?>) postApplicationResource.post(form,
                HTML_VARIANT);
        assertValidationFailure(postApplicationResource, post);
    }
    
    @Test
    public void projectName_is_set_if_not_provided() {
        DbApplication app = createValidApplication();
        app.setProjectName("");

        SkysailResponse<DbApplication> response = postApplicationResource.post(app, JSON_VARIANT);
        
        setAttributes("id", response.getEntity().getId());
        init(applicationResource);
        DbApplication appFromDb = applicationResource.getEntity();
        app.setProjectName("skysail.server.app." + app.getName());
        assertListResult(postApplicationResource, appFromDb, app, Status.SUCCESS_CREATED);
    }

    @Test
    public void packageName_is_set_if_not_provided() {
        DbApplication app = createValidApplication();
        app.setPackageName("");

        SkysailResponse<DbApplication> response = postApplicationResource.post(app, JSON_VARIANT);
        
        setAttributes("id", response.getEntity().getId());
        init(applicationResource);
        DbApplication appFromDb = applicationResource.getEntity();
        app.setPackageName("io.skysail.server.app." + app.getName());
        assertListResult(postApplicationResource, appFromDb, app, Status.SUCCESS_CREATED);
    }

    @Test
    public void path_is_set_if_not_provided() {
        DbApplication app = createValidApplication();
        app.setPath("");

        SkysailResponse<DbApplication> response = postApplicationResource.post(app, JSON_VARIANT);
        
        setAttributes("id", response.getEntity().getId());
        init(applicationResource);
        DbApplication appFromDb = applicationResource.getEntity();
        app.setPath("../");
        assertListResult(postApplicationResource, appFromDb, app, Status.SUCCESS_CREATED);
    }

}
