package io.skysail.server.app.designer.model.test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import org.junit.*;
import org.junit.rules.ExpectedException;

import io.skysail.server.app.designer.entities.Entity;
import io.skysail.server.app.designer.model.CodegenEntityModel;

public class CodegenEntityModelTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    private CodegenEntityModel entityModel;

    @Before
    public void setUp() throws Exception {
        entityModel = new CodegenEntityModel(new Entity("name"), "pkg");
    }

    @Test
    public void testName() {
        assertThat(entityModel.getId(),is(equalTo("pkg.name")));
    }

//    @Test
//    public void adding_field_succeeds() {
//        EntityField entityField = new EntityField();
//        entityField.setName("entityField");
//        entityModel.addField(entityField);
//        assertThat(entityModel.getFields().size(),is(1));
//    }
//    
//    @Test
//    public void adding_field_twice_throws_exception() {
//        thrown.expect(IllegalStateException.class);
//        EntityField entityField = new EntityField();
//        entityField.setName("entityField");
//        entityModel.addField(entityField);
//        entityModel.addField(entityField);
//    }
}
