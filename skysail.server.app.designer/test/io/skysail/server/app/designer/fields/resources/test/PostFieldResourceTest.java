package io.skysail.server.app.designer.fields.resources.test;

import java.io.IOException;

import org.junit.Test;
import org.restlet.data.Status;

import io.skysail.api.forms.InputType;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.fields.DbEntityField;
import io.skysail.server.app.designer.test.utils.YamlTestFileReader;

public class PostFieldResourceTest extends AbstractFieldResourceTest {

    @Test
    public void adds_field_by_form_to_empty_entity() throws Exception {
        DbApplication application = prepareApplication();
        setAttributes("eid", application.getEntities().get(0).getId());
        form.add("name", "TestField");
        form.add("type", "TEXT");
        form.add("notNull", "on");

        SkysailResponse<DbEntityField> result = postFieldResource.post(form, HTML_VARIANT);
        
        DbEntityField expectedDbField = DbEntityField.builder().name("TestField").type(InputType.TEXT).notNull(true).build();
        assertListResult(postFieldResource, result, expectedDbField, Status.REDIRECTION_SEE_OTHER);
    }

    @Test
    public void adds_field_with_json_to_empty_entity() throws Exception {
        DbApplication application = prepareApplication();
        setAttributes("eid", application.getEntities().get(0).getId());
        DbEntityField dbField = DbEntityField.builder().name("TestField").type(InputType.TEXT).notNull(true).build();

        SkysailResponse<DbEntityField> result = postFieldResource.post(dbField, HTML_VARIANT);
        
        assertListResult(postFieldResource, result, dbField, Status.REDIRECTION_SEE_OTHER);
    }

    private DbApplication prepareApplication() throws IOException, Exception {
        DbApplication appFromFile = YamlTestFileReader.read("fieldtests", "checklistWithEntityWithoutFields.yml");
        SkysailResponse<DbApplication> response = postApplicationResource.post(appFromFile, JSON_VARIANT);
        setAttributes("id", response.getEntity().getId().replace("#", ""));
        setUpResource(applicationResource, application.getContext());
        return applicationResource.getEntity();
    }

}
