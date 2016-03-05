package io.skysail.server.app.designer.model.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import io.skysail.server.app.designer.entities.DbEntity;
import io.skysail.server.app.designer.model.DesignerEntityModel;

@Ignore
public class DesignerEntityModelTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    private DesignerEntityModel entityModel;

    @Before
    public void setUp() throws Exception {
        entityModel = new DesignerEntityModel(new DbEntity("name"), "pkg");
    }

    @Test
    public void testName() {
        assertThat(entityModel.getId(),is(equalTo("pkg.name")));
    }
    
    @Test
    public void getAggregateRoot_is_self_if_isAggregate_is_true() {
        entityModel.setAggregate(true);
        assertThat(entityModel.getAggregateRoot(),is(entityModel));
        
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
