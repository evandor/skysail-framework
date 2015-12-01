package io.skysail.server.app.designer.model.test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import org.junit.*;

import io.skysail.server.app.designer.entities.Entity;
import io.skysail.server.app.designer.model.*;

public class ReferenceModelTest {

    private ReferenceModel referenceModel;

    @Before
    public void setUp()  {
        Entity entity = new Entity("entityName");
        CodegenEntityModel entityModel = new CodegenEntityModel(entity, "pgk");
        referenceModel = new ReferenceModel(entityModel, entity);
    }

    @Test
    public void testName() {
        assertThat(referenceModel.getReferencedEntityName(),is(equalTo("entityName")));
    }

}
