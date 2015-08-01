package de.twenty11.skysail.server.core.test;

import io.skysail.api.forms.Reference;
import io.skysail.api.links.LinkRelation;
import io.skysail.server.restlet.resources.SkysailServerResource;

import org.junit.Before;

public class FormFieldTest {

    public String test;

    @io.skysail.api.forms.Field(selectionProvider = DummySelectionProvider.class)
    public String testField;

    @Reference(selectionProvider = DummySelectionProvider.class)
    public String testReference;

    private SkysailServerResource<?> resource;

    @Before
    public void setUp() {
        resource = new SkysailServerResource<String>() {

            @Override
            public String getEntity() {
                return null;
            }

            @Override
            public LinkRelation getLinkRelation() {
                return null;
            }
        };
    }

//    @Test
//    public void test_is_not_a_selectionProvider() throws Exception {
//        Field field = FormFieldTest.class.getField("test");
//        FormField formField = new FormField(field, resource, "someString");
//        assertThat(formField.isSelectionProvider(), is(false));
//    }
//
//    @Test
//    public void testField_is_selectionProvider() throws Exception {
//        Field field = FormFieldTest.class.getField("testField");
//        FormField formField = new FormField(field, resource, "someString");
//        assertThat(formField.isSelectionProvider(), is(true));
//    }
//
//    @Test
//    public void testReference_is_selectionProvider() throws Exception {
//        Field field = FormFieldTest.class.getField("testReference");
//        FormField formField = new FormField(field, resource, "someString");
//        assertThat(formField.isSelectionProvider(), is(true));
//    }
//
//    @Test
//    public void testField_get_selectionOptions() throws Exception {
//        Field field = FormFieldTest.class.getField("testField");
//        FormField formField = new FormField(field, resource, "someString");
//        assertThat(formField.getSelectionProviderOptions().size(), is(1));
//    }
//
//    @Test
//    public void testReference_gets_selectionOptions() throws Exception {
//        Field field = FormFieldTest.class.getField("testReference");
//        FormField formField = new FormField(field, resource, "someString");
//        assertThat(formField.getSelectionProviderOptions().size(), is(1));
//    }

}
