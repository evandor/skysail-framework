package io.skysail.server.app.designer.entities.resources.test;

import org.junit.*;
import org.restlet.data.*;
import org.restlet.engine.resource.VariantInfo;

import io.skysail.api.responses.*;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.entities.DbEntity;

public class PostEntityResourceTest extends AbstractEntityResourceTest {

    private DbApplication anApplication;
    
    @Before
    public void setUp() throws Exception {
        super.setUp();
        anApplication = createApplication();
        setAttributes("id", anApplication.getId());
    }

    @Test
    public void empty_form_data_yields_validation_failure() {
        ConstraintViolationsResponse<?> post = (ConstraintViolationsResponse<?>) postEntityResource.post(form,
                HTML_VARIANT);
        assertValidationFailure(postEntityResource, post);
    }

    @Test
    public void empty_json_data_yields_validation_failure() {
        ConstraintViolationsResponse<?> post = (ConstraintViolationsResponse<?>) postEntityResource.post(
                new DbEntity(), JSON_VARIANT);
        assertValidationFailure(postEntityResource, post);
    }

    @Test
    public void valid_form_data_yields_new_entity() {
        form.add("name", "TestEntity");
        form.add("rootEntity", "on");

        SkysailResponse<DbEntity> result = postEntityResource.post(form, HTML_VARIANT);
        assertListResult(postEntityResource, result, DbEntity.builder().name("TestEntity").rootEntity(true).build(), Status.REDIRECTION_SEE_OTHER);
    }

//    @Test
//    public void valid_json_data_yields_new_entity() {
//        DbEntity app = createValidEntity();
//        SkysailResponse<DbEntity> result = postEntityResource.post(app, JSON_VARIANT);
//        assertThat(responses.get(postEntityResource.getClass().getName()).getStatus(),
//                is(equalTo(Status.SUCCESS_CREATED)));
//        assertListResult(postEntityResource, result, app);
//    }

    @Test
    public void two_entries_with_same_name_yields_failure() {
        form.add("name", "list2");
        postEntityResource.post(form, new VariantInfo(MediaType.TEXT_HTML));
        ConstraintViolationsResponse<?> post = (ConstraintViolationsResponse<?>) postEntityResource.post(form,
                HTML_VARIANT);
        assertValidationFailure(postEntityResource, post);
    }



}
