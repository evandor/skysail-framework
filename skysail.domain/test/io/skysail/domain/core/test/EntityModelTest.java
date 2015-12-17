package io.skysail.domain.core.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.domain.core.*;

import org.junit.*;

public class EntityModelTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void construct_from_full_path_initializes_correct() {
        EntityModel entity = new EntityModel("io.skysail.domain.core.test.AThing");
        assertThat(entity.getFields().size(),is(0));
        assertThat(entity.getPackageName(),is("io.skysail.domain.core.test"));
        assertThat(entity.getSimpleName(), is("AThing"));
    }

    @Test
    public void construct_from_default_path_initializes_correct() {
        EntityModel entity = new EntityModel("AThing");
        assertThat(entity.getFields().size(),is(0));
        assertThat(entity.getPackageName(),is(""));
        assertThat(entity.getSimpleName(), is("AThing"));
    }

    @Test
    public void added_field_can_be_retrieved_again() {
        EntityModel entity = new EntityModel("AThing");
        FieldModel field = new FieldModel("fieldname");
        entity.add(field);
        assertThat(entity.getFields().size(),is(1));
        assertThat(entity.getField("fieldname").getId(), is("fieldname"));
    }
}
