package io.skysail.server.app.designer.model.test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.server.app.designer.entities.Entity;
import io.skysail.server.app.designer.model.ReferenceModel;

import org.junit.*;

public class ReferenceModelTest {

    private ReferenceModel referenceModel;

    @Before
    public void setUp()  {
        Entity entity = new Entity("entityName");
        referenceModel = new ReferenceModel(entity);
    }

    @Test
    public void testName() {
        assertThat(referenceModel.getName(),is(equalTo("entityName")));
    }

}
