package de.twenty11.skysail.server.core.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;

import org.junit.Test;

import de.twenty11.skysail.api.forms.Reference;
import de.twenty11.skysail.server.core.FormField;

public class FormFieldTest {

    @de.twenty11.skysail.api.forms.Field(selectionProvider = DummySelectionProvider.class)
    public String testField;

    @Reference(selectionProvider = DummySelectionProvider.class, cls = String.class)
    public String testReference;

    public String test;

    @Test
    public void field_test_is_not_a_selectionProvider() throws Exception {
        Field field = FormFieldTest.class.getField("test");
        FormField formField = new FormField(field, null, null, "someString");
        assertThat(formField.isSelectionProvider(), is(false));
    }

    @Test
    public void testField_is_selectionProvider() throws Exception {
        Field field = FormFieldTest.class.getField("testField");
        FormField formField = new FormField(field, null, null, "someString");
        assertThat(formField.isSelectionProvider(), is(true));
    }

    @Test
    public void testReference_is_selectionProvider() throws Exception {
        Field field = FormFieldTest.class.getField("testReference");
        FormField formField = new FormField(field, null, null, "someString");
        assertThat(formField.isSelectionProvider(), is(true));
    }

    @Test
    public void testField_get_selectionOptions() throws Exception {
        Field field = FormFieldTest.class.getField("testField");
        FormField formField = new FormField(field, null, null, "someString");
        assertThat(formField.getSelectionProviderOptions().size(), is(1));
    }

    @Test
    public void testReference_gets_selectionOptions() throws Exception {
        Field field = FormFieldTest.class.getField("testReference");
        FormField formField = new FormField(field, null, null, "someString");
        assertThat(formField.getSelectionProviderOptions().size(), is(1));
    }

}
