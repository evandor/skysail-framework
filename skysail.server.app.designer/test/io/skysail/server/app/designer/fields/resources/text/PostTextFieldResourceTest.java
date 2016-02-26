package io.skysail.server.app.designer.fields.resources.text;

import org.junit.Ignore;
import org.junit.Test;
import org.restlet.data.Status;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.fields.*;
import io.skysail.server.app.designer.fields.resources.test.AbstractFieldResourceTest;
import io.skysail.server.testsupport.FormBuilder;

@Ignore
public class PostTextFieldResourceTest extends AbstractFieldResourceTest {
    
    @Test
    public void adds_field_by_form_to_empty_entity() throws Exception {
        DbApplication application = prepareApplication("checklistWithEntityWithoutFields.yml", "PostFieldResourceTest1");
        setAttributes("eid", application.getEntities().get(0).getId());
        
        SkysailResponse<DbEntityTextField> response = postTextFieldResource.post(
            new FormBuilder()
                .add("name", "TestField")
                .add("type", "TEXT")
                .add("notNull", "on").build(), HTML_VARIANT);
        
        DbEntityField expectedDbField = DbEntityTextField.builder().name("TestField").mandatory(true).build();
        assertListResult(postTextFieldResource, response, expectedDbField, Status.REDIRECTION_SEE_OTHER);
    }

    @Test
    public void adds_field_with_json_to_empty_entity() throws Exception {
        DbApplication application = prepareApplication("checklistWithEntityWithoutFields.yml", "PostFieldResourceTest2");
        setAttributes("eid", application.getEntities().get(0).getId());
        DbEntityTextField dbField = DbEntityTextField.builder()
                .name("TestField")
                .mandatory(true).build();

        SkysailResponse<DbEntityTextField> result = postTextFieldResource.post(dbField, HTML_VARIANT);
        
        assertListResult(postTextFieldResource, result, dbField, Status.REDIRECTION_SEE_OTHER);
    }

}
