package io.skysail.server.forms.test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import io.skysail.api.forms.Reference;
import io.skysail.api.links.LinkRelation;
import io.skysail.domain.Identifiable;
import io.skysail.server.forms.FormField;
import io.skysail.server.restlet.resources.SkysailServerResource;

import java.lang.reflect.Field;

import org.junit.*;

import de.twenty11.skysail.server.core.test.DummySelectionProvider;

public class FormFieldTest {

    public String test;

    @io.skysail.api.forms.Field(selectionProvider = DummySelectionProvider.class)
    public String testField;

    @Reference(selectionProvider = DummySelectionProvider.class)
    public String testReference;

    private SkysailServerResource<?> resource;

    @Before
    public void setUp() {
        resource = new SkysailServerResource<Identifiable>() {

            @Override
            public Identifiable getEntity() {
                return null;
            }

            @Override
            public LinkRelation getLinkRelation() {
                return null;
            }
        };
    }

    @Test
    public void test_is_not_a_selectionProvider() throws Exception {
        Field field = FormFieldTest.class.getField("test");
        FormField formField = new FormField(field, resource);
        assertThat(formField.isSelectionProvider(), org.hamcrest.Matchers.is(false));
    }

    @Test
    public void testField_is_selectionProvider() throws Exception {
        Field field = FormFieldTest.class.getField("testField");
        FormField formField = new FormField(field, resource);
        assertThat(formField.isSelectionProvider(), is(true));
    }

    @Test
    public void testReference_is_selectionProvider() throws Exception {
        Field field = FormFieldTest.class.getField("testReference");
        FormField formField = new FormField(field, resource);
        assertThat(formField.isSelectionProvider(), is(true));
    }

    @Test
    public void testField_get_selectionOptions() throws Exception {
        Field field = FormFieldTest.class.getField("testField");
        FormField formField = new FormField(field, resource);
        assertThat(formField.getSelectionProviderOptions().size(), is(1));
    }

    @Test
    public void testReference_gets_selectionOptions() throws Exception {
        Field field = FormFieldTest.class.getField("testReference");
        FormField formField = new FormField(field, resource);
        assertThat(formField.getSelectionProviderOptions().size(), is(1));
    }
}
