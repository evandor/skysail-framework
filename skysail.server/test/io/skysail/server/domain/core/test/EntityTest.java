package io.skysail.server.domain.core.test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.api.domain.Identifiable;
import io.skysail.server.domain.core.Entity;

import org.junit.*;

public class EntityTest {

    private AThing aThing;
    private Class<? extends Identifiable> identifiableClass;

    @Before
    public void setUp() throws Exception {
        aThing = new AThing();
        identifiableClass = AThing.class;
    }

    @Test
    public void id_is_set_in_string_constructor() {
        Entity entity = new Entity(aThing.getClass().getSimpleName());
        assertThat(entity.getId(),is("AThing"));
    }

    @Test
    public void id_is_set_in_class_constructor() {
        Class<? extends Identifiable> cls = AThing.class;
        Entity entity = new Entity(cls);
        assertThat(entity.getId(),is("AThing"));
    }

    @Test
    public void post_resource_is_found() {
        Entity entity = new Entity(identifiableClass);
        assertThat(entity.getPostResourceClass().toString(),is(PostAThingResource.class.toString()));
    }

    @Test
    public void put_resource_is_found() {
        Entity entity = new Entity(identifiableClass);
        assertThat(entity.getPutResourceClass().toString(),is(PutAThingResource.class.toString()));
    }

    @Test
    public void list_resource_is_found() {
        Entity entity = new Entity(identifiableClass);
        assertThat(entity.getListResourceClass().toString(),is(AThingsResource.class.toString()));
    }

    @Test
    public void entity_resource_is_found() {
        Entity entity = new Entity(identifiableClass);
        assertThat(entity.getEntityResourceClass().toString(),is(AThingResource.class.toString()));
    }


}
