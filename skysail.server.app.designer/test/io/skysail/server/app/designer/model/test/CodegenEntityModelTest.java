package io.skysail.server.app.designer.model.test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import org.junit.*;
import org.junit.rules.ExpectedException;

import io.skysail.server.app.designer.entities.DbEntity;
import io.skysail.server.app.designer.model.CodegenEntityModel;

public class CodegenEntityModelTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    private CodegenEntityModel entityModel;

    @Before
    public void setUp() throws Exception {
        entityModel = new CodegenEntityModel(new DbEntity("name"), "pkg");
    }

    @Test
    public void testName() {
        assertThat(entityModel.getId(),is(equalTo("pkg.name")));
    }

//    @Test
//    public void adding_field_succeeds() {
//        DbEntityField entityField = new DbEntityField();
//        entityField.setName("entityField");
//        entityModel.addField(entityField);
//        assertThat(entityModel.getFields().size(),is(1));
//    }
//    
//    @Test
//    public void adding_field_twice_throws_exception() {
//        thrown.expect(IllegalStateException.class);
//        DbEntityField entityField = new DbEntityField();
//        entityField.setName("entityField");
//        entityModel.addField(entityField);
//        entityModel.addField(entityField);
//    }
}
