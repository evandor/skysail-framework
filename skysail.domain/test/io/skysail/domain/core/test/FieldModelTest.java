package io.skysail.domain.core.test;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

import org.junit.*;

import io.skysail.domain.core.FieldModel;

public class FieldModelTest {

    @Before
    public void setUp() throws Exception {
    }
    
    @Test
    public void testName() {
        FieldModel fieldModel = new FieldModel("fieldModel");
        assertThat(fieldModel.toString(), containsString("fieldModel"));
    }

    @Test
    public void name_is_equal_to_id() {
        FieldModel fieldModel = new FieldModel("fieldModel");
        assertThat(fieldModel.getId(), containsString("fieldModel"));
        assertThat(fieldModel.getName(), containsString("fieldModel"));
    }

}
