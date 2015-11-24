package io.skysail.server.domain.core.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.server.domain.*;
import io.skysail.server.domain.core.*;

import org.junit.*;

public class EntityTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void construct_from_full_path_initializes_correct() {
        Entity entity = new Entity("io.skysail.server.domain.core.test.AThing");
        assertThat(entity.getFields().size(),is(0));
        assertThat(entity.getPackageName(),is("io.skysail.server.domain.core.test"));
        assertThat(entity.getSimpleName(), is("AThing"));
    }

    @Test
    public void construct_from_default_path_initializes_correct() {
        Entity entity = new Entity("AThing");
        assertThat(entity.getFields().size(),is(0));
        assertThat(entity.getPackageName(),is(""));
        assertThat(entity.getSimpleName(), is("AThing"));
    }

    @Test
    public void added_field_can_be_retrieved_again() {
        Entity entity = new Entity("AThing");
        Field field = new Field("fieldname");
        entity.add(field);
        assertThat(entity.getFields().size(),is(1));
        assertThat(entity.getField("fieldname").getId(), is("fieldname"));
    }
}
