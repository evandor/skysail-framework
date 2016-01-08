package io.skysail.server.domain.jvm.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;

import org.junit.*;

import io.skysail.server.domain.jvm.ClassFieldModel;

public class ClassFieldModelTest {

    private ClassFieldModel classFieldModel;
    
    @io.skysail.domain.html.Field
    private String aField;

    @Before
    public void setUp() throws Exception {
        Field field = this.getClass().getDeclaredField("aField");
        classFieldModel = new ClassFieldModel(field);
    }

    @Test
    public void toString_is_formatted_nicely() {
        String[] toString = classFieldModel.toString().split("\\n");
        assertThat(toString[0],is("ClassFieldModel(id=aField, type=String, inputType=TEXT)"));
    }
}
