package io.skysail.server.app.designer.model.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import io.skysail.server.app.designer.entities.DbEntity;
import io.skysail.server.app.designer.fields.DbEntityField;
import io.skysail.server.app.designer.fields.DbEntityTextField;
import io.skysail.server.app.designer.model.DesignerEntityModel;
import io.skysail.server.app.designer.model.DesignerFieldModel;

public class DesignerFieldModelTest {

    private DesignerFieldModel fieldModel;

    @Before
    public void setUp()  {
        DbEntityField field = new DbEntityTextField("fieldName", false);
        DbEntity entityFromDb = new DbEntity();
        DesignerEntityModel entityModel = new DesignerEntityModel(entityFromDb, "packageName");
        fieldModel = new DesignerFieldModel(entityModel, field);
    }

    @Test
    public void testName() {
        assertThat(fieldModel.getName(),is(equalTo("fieldName")));
    }

}
