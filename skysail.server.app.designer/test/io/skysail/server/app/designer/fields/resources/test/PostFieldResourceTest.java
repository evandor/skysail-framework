package io.skysail.server.app.designer.fields.resources.test;

import org.junit.Test;
import org.restlet.data.Status;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.domain.html.InputType;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.fields.DbEntityField;
import io.skysail.server.testsupport.FormBuilder;

public class PostFieldResourceTest extends AbstractFieldResourceTest {
    
    @Test
    public void adds_field_by_form_to_empty_entity() throws Exception {
        DbApplication application = prepareApplication("checklistWithEntityWithoutFields.yml", "PostFieldResourceTest1");
        setAttributes("eid", application.getEntities().get(0).getId());
        
        SkysailResponse<DbEntityField> response = postFieldResource.post(
            new FormBuilder()
                .add("name", "TestField")
                .add("type", "TEXT")
                .add("notNull", "on").build(), HTML_VARIANT);
        
        DbEntityField expectedDbField = DbEntityField.builder().name("TestField").type(InputType.TEXT).notNull(true).build();
        assertListResult(postFieldResource, response, expectedDbField, Status.REDIRECTION_SEE_OTHER);
    }

    @Test
    public void adds_field_with_json_to_empty_entity() throws Exception {
        DbApplication application = prepareApplication("checklistWithEntityWithoutFields.yml", "PostFieldResourceTest2");
        setAttributes("eid", application.getEntities().get(0).getId());
        DbEntityField dbField = DbEntityField.builder()
                .name("TestField")
                .type(InputType.TEXT)
                .notNull(true).build();

        SkysailResponse<DbEntityField> result = postFieldResource.post(dbField, HTML_VARIANT);
        
        assertListResult(postFieldResource, result, dbField, Status.REDIRECTION_SEE_OTHER);
    }

}
