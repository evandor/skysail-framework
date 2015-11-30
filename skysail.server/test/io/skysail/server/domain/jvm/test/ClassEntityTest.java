package io.skysail.server.domain.jvm.test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.api.domain.Identifiable;
import io.skysail.server.domain.core.EntityModel;
import io.skysail.server.domain.jvm.ClassEntity;

import org.junit.*;

public class ClassEntityTest {

    private AThing aThing;
    private Class<? extends Identifiable> identifiableClass;

    @Before
    public void setUp() throws Exception {
        aThing = new AThing();
        identifiableClass = AThing.class;
    }

    @Test
    public void id_is_set_in_string_constructor() {
        EntityModel entity = new EntityModel(aThing.getClass().getSimpleName());
        assertThat(entity.getId(),is("AThing"));
    }

    @Test
    public void id_is_set_in_class_constructor() {
        Class<? extends Identifiable> cls = AThing.class;
        ClassEntity entity = new ClassEntity(cls);
        assertThat(entity.getId(),is(AThing.class.getName()));
    }

    @Test
    public void simple_stringField_is_detected_correctly() throws Exception {
        ClassEntity entity = new ClassEntity(identifiableClass);
        assertThat(entity.getFields().size(), is(1));
        assertThat(entity.getFields().get("stringField").getId(),is("stringField"));
    }


}
