package io.skysail.server.app.designer.fields.resources.text;

import org.junit.Test;
import org.restlet.data.Status;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.fields.*;
import io.skysail.server.app.designer.fields.resources.test.AbstractFieldResourceTest;
import io.skysail.server.testsupport.FormBuilder;

public class PostTextFieldResourceTest extends AbstractFieldResourceTest {
    
    @Test
    public void adds_field_by_form_to_empty_entity() throws Exception {
        DbApplication application = prepareApplication("checklistWithEntityWithoutFields.yml", "PostFieldResourceTest1");
        setAttributes("eid", application.getEntities().get(0).getId());
        
        SkysailResponse<DbEntityField> response = postFieldResource.post(
            new FormBuilder()
                .add("name", "TestField")
                .add("type", "TEXT")
                .add("notNull", "on").build(), HTML_VARIANT);
        
        DbEntityField expectedDbField = DbEntityTextField.builder().name("TestField").mandatory(true).build();
        assertListResult(postFieldResource, response, expectedDbField, Status.REDIRECTION_SEE_OTHER);
    }

    @Test
    public void adds_field_with_json_to_empty_entity() throws Exception {
        DbApplication application = prepareApplication("checklistWithEntityWithoutFields.yml", "PostFieldResourceTest2");
        setAttributes("eid", application.getEntities().get(0).getId());
        DbEntityField dbField = DbEntityTextField.builder()
                .name("TestField")
                .mandatory(true).build();

        SkysailResponse<DbEntityField> result = postFieldResource.post(dbField, HTML_VARIANT);
        
        assertListResult(postFieldResource, result, dbField, Status.REDIRECTION_SEE_OTHER);
    }

}
