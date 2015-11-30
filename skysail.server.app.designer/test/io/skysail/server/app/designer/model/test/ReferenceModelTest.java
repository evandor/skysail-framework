package io.skysail.server.app.designer.model.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.server.app.designer.entities.Entity;
import io.skysail.server.app.designer.model.CodegenEntityModel;
import io.skysail.server.app.designer.model.ReferenceModel;

import org.junit.Before;
import org.junit.Test;

public class ReferenceModelTest {

    private ReferenceModel referenceModel;

    @Before
    public void setUp()  {
        Entity entity = new Entity("entityName");
        CodegenEntityModel entityModel = new CodegenEntityModel(entity);
        referenceModel = new ReferenceModel(entityModel, entity);
    }

    @Test
    public void testName() {
        assertThat(referenceModel.getReferencedEntityName(),is(equalTo("entityName")));
    }

}
