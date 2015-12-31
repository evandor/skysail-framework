package io.skysail.server.app.designer.model.test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import org.junit.*;

import io.skysail.server.app.designer.fields.*;
import io.skysail.server.app.designer.model.CodegenFieldModel;

public class CodegenFieldModelTest {

    private CodegenFieldModel fieldModel;

    @Before
    public void setUp()  {
        DbEntityField field = new DbEntityTextField("fieldName", false);
        fieldModel = new CodegenFieldModel(field);
    }

    @Test
    public void testName() {
        assertThat(fieldModel.getName(),is(equalTo("fieldName")));
    }

}
