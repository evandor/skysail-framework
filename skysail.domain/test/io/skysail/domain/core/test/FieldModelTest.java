package io.skysail.domain.core.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

import org.junit.*;

import io.skysail.domain.core.FieldModel;
import io.skysail.domain.html.InputType;

public class FieldModelTest {

    private FieldModel fieldModel;

    @Before
    public void setUp() {
        fieldModel = new FieldModel("fieldModel");
    }
    
    @Test
    public void name_is_equal_to_id() {
        assertThat(fieldModel.getId(), containsString("fieldModel"));
        assertThat(fieldModel.getName(), containsString("fieldModel"));
    }
    
    @Test
    public void toString_is_formatted_nicely() {
        fieldModel.setInputType(InputType.BUTTON);
        fieldModel.setMandatory(true);
        fieldModel.setReadonly(false);
        fieldModel.setType(String.class);
        String[] toString = fieldModel.toString().split("\\n");
        assertThat(toString[0],is("FieldModel(id=fieldModel, type=String, inputType=BUTTON)"));
    }

}
