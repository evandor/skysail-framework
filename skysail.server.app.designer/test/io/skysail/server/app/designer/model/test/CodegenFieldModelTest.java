package io.skysail.server.app.designer.model.test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.server.app.designer.fields.DbEntityField;
import io.skysail.server.app.designer.model.CodegenFieldModel;

import org.junit.*;

public class CodegenFieldModelTest {

    private CodegenFieldModel fieldModel;

    @Before
    public void setUp()  {
        DbEntityField field = new DbEntityField();
        field.setName("fieldName");
        fieldModel = new CodegenFieldModel(field);
    }

    @Test
    public void testName() {
        assertThat(fieldModel.getName(),is(equalTo("fieldName")));
    }

}
