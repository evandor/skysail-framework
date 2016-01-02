package io.skysail.server.app.designer.fields.resources.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.*;

import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.entities.DbEntity;
import io.skysail.server.app.designer.fields.DbEntityField;

@Ignore // FIXME
public class FieldResourceTest extends AbstractFieldResourceTest {
    
    @Test
    public void retrieves_entities_fields() throws Exception {
        DbApplication dbApplication = prepareApplication("checklistWithEntityWithOneField.yml", "FieldResourceTest1");
        addAttribute("eid", dbApplication.getEntities().get(0).getId());
        addAttribute("fieldId", dbApplication.getEntities().get(0).getFields().get(0).getId());
        init(fieldResource);
        
        DbEntityField field = fieldResource.getEntity();
        
        assertThat(field.getName(), is("listname"));
    }
    
    @Test
    public void deletes_entities_field() throws Exception {
        DbApplication dbApplication = prepareApplication("checklistWithEntityWithOneField.yml", "FieldResourceTest2");
        DbEntity firstEntity = dbApplication.getEntities().get(0);
        addAttribute("eid", firstEntity.getId());
        addAttribute("fieldId", firstEntity.getFields().get(0).getId());
        init(fieldResource);
        
        fieldResource.eraseEntity();
        
        init(fieldsResource);
        List<DbEntityField> entities = fieldsResource.getEntity();
        assertThat(entities.size(), is(0));
    }

}
