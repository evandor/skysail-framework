package io.skysail.server.app.designer.entities.resources.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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
        
        DbEntity expectedDbEntity = DbEntity.builder().name("TestEntity").rootEntity(true).build();
        assertListResult(postEntityResource, result, expectedDbEntity, Status.REDIRECTION_SEE_OTHER);
    }

    @Test
    public void valid_json_data_yields_new_entity() {
        DbEntity entity = DbEntity.builder().name("AnEntity").rootEntity(true).build();
        
        SkysailResponse<DbEntity> result = postEntityResource.post(entity, JSON_VARIANT);
        
        assertThat(responses.get(postEntityResource.getClass().getName()).getStatus(),
                is(Status.SUCCESS_CREATED));
        assertListResult(postEntityResource, result, entity, Status.SUCCESS_CREATED);
    }

    @Test
    public void two_entities_can_be_added() {
        DbEntity entity1 = DbEntity.builder().name("AnEntity").rootEntity(true).build();
        DbEntity entity2 = DbEntity.builder().name("AnotherEntity").rootEntity(true).build();
        
        SkysailResponse<DbEntity> result = postEntityResource.post(entity1, JSON_VARIANT);
        SkysailResponse<DbEntity> result2 = postEntityResource.post(entity2, JSON_VARIANT);
        
        assertThat(responses.get(postEntityResource.getClass().getName()).getStatus(),
                is(Status.SUCCESS_CREATED));
        assertListResult(postEntityResource, result, entity1, Status.SUCCESS_CREATED);
    }

    @Test
    public void two_entries_with_same_name_yields_failure() {
        form.add("name", "list2");
        postEntityResource.post(form, new VariantInfo(MediaType.TEXT_HTML));
        ConstraintViolationsResponse<?> post = (ConstraintViolationsResponse<?>) postEntityResource.post(form,
                HTML_VARIANT);
        assertValidationFailure(postEntityResource, post);
    }



}
