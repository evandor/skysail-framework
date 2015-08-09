package io.skysail.server.model.test;

import io.skysail.server.forms.FormField;

import java.util.*;

import org.junit.Before;

public class EntityModelTest {

    private List<FormField> formfields;

    @Before
    public void setUp() throws Exception {
        formfields = new ArrayList<>();
    }
    

//    @Test
//    public void no_fields() {
//        FormField formfield = new FormField("key", "anObject");
//        formfields.add(formfield);
//        EntityModel entityModel = new EntityModel(formfields);
//        Map<String, Object> props = new HashMap<>();
//        SkysailServerResource<?> resource = Mockito.mock(SkysailServerResource.class);
//        assertThat(entityModel.dataFromMap(props, resource), is(equalTo(props)));
//    }
}
