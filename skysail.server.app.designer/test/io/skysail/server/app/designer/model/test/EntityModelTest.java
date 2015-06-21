package io.skysail.server.app.designer.model.test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.server.app.designer.fields.EntityField;
import io.skysail.server.app.designer.model.EntityModel;

import org.junit.*;
import org.junit.rules.ExpectedException;

public class EntityModelTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    private EntityModel entityModel;

    @Before
    public void setUp() throws Exception {
        entityModel = new EntityModel("name");
    }

    @Test
    public void testName() {
        assertThat(entityModel.getEntityName(),is(equalTo("name")));
    }

    @Test
    public void adding_field_succeeds() {
        EntityField entityField = new EntityField();
        entityField.setName("entityField");
        entityModel.addField(entityField);
        assertThat(entityModel.getFields().size(),is(1));
    }
    
    @Test
    public void adding_field_twice_throws_exception() {
        thrown.expect(IllegalStateException.class);
        EntityField entityField = new EntityField();
        entityField.setName("entityField");
        entityModel.addField(entityField);
        entityModel.addField(entityField);
    }
}
