package io.skysail.server.app.designer.fields.resources.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.*;

import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.fields.DbEntityField;

@Ignore // FIXME
public class FieldsResourceTest extends AbstractFieldResourceTest {
    
    @Test
    public void testName() throws Exception {
        DbApplication dbApplication = prepareApplication("checklistWithEntityWithOneField.yml", "FieldsResourceTest1");
        setAttributes("eid", dbApplication.getEntities().get(0).getId());
        init(fieldsResource);
        
        List<DbEntityField> fields = fieldsResource.getEntity();
        
        assertThat(fields.size(), is(1));
        assertThat(fields.get(0).getName(), is("listname"));
    }

}
