package io.skysail.server.app.designer.model.test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import org.junit.*;

import io.skysail.server.app.designer.entities.DbEntity;
import io.skysail.server.app.designer.model.*;

public class ReferenceModelTest {

    private ReferenceModel referenceModel;

    @Before
    public void setUp()  {
        DbEntity entity = new DbEntity("entityName");
        CodegenEntityModel entityModel = new CodegenEntityModel(entity, "pgk");
        referenceModel = new ReferenceModel(entityModel, entity);
    }

    @Test
    public void testName() {
        assertThat(referenceModel.getReferencedEntityName(),is(equalTo("entityName")));
    }

}
