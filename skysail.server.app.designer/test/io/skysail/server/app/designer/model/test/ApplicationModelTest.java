package io.skysail.server.app.designer.model.test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.server.app.designer.entities.Entity;
import io.skysail.server.app.designer.model.*;

import org.junit.*;
import org.junit.rules.ExpectedException;

public class ApplicationModelTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private ApplicationModel applicationModel;

    @Before
    public void setUp() throws Exception {
        applicationModel = new ApplicationModel("name");
    }

    @Test
    public void testName() {
        assertThat(applicationModel.getApplicationName(), is(equalTo("name")));
    }

    @Test
    public void adding_entity_succeeds() {
        EntityModel addedEntity = applicationModel.addEntity("entityName");
        assertThat(addedEntity.getEntityName(), is(equalTo("entityName")));
    }

    @Test
    public void adding_entity_twice_throws_exception() {
        thrown.expect(IllegalStateException.class);
        applicationModel.addEntity("entityName");
        applicationModel.addEntity("entityName");
    }

    @Test
    public void validation_succeeds_for_reference_with_known_entity() {
        EntityModel entity = applicationModel.addEntity("entityName");
        Entity unknownEntity = new Entity("entityName");
        entity.addReference(unknownEntity);
        applicationModel.validate();
    }

    @Test
    public void validation_throws_expection_for_reference_with_unknown_entity() {
        thrown.expect(IllegalStateException.class);
        EntityModel entity = applicationModel.addEntity("entityName");
        Entity unknownEntity = new Entity("unknown");
        entity.addReference(unknownEntity);
        applicationModel.validate();
    }

}
