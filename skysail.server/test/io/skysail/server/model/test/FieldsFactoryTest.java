package io.skysail.server.model.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;
import io.skysail.api.responses.FormResponse;
import io.skysail.server.model.*;
import io.skysail.server.restlet.resources.SkysailServerResource;

import java.util.ArrayList;

import org.junit.*;

public class FieldsFactoryTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void null_source_yields_NoFieldFactory() {
        assertThat(FieldsFactory.getFactory(null, (SkysailServerResource<?>) new TestListResource()),
                is(instanceOf(NoFieldFactory.class)));
    }

    @Test
    public void list_source_yields_defaultListFieldFactory() {
        assertThat(FieldsFactory.getFactory(new ArrayList<>(), (SkysailServerResource<?>) new TestListResource()),
                is(instanceOf(DefaultListFieldFactory.class)));
    }

    @Test
    public void list_source_of_MapEntity_yields_defaultListFieldFactory() {
        assertThat(FieldsFactory.getFactory(new ArrayList<>(), (SkysailServerResource<?>) new TestListOfMapResource()),
                is(instanceOf(ListMapFieldFactory.class)));
    }

    @Test
    public void list_source_of_EnumEntity_yields_ListEnumFieldFactory() {
        assertThat(
                FieldsFactory.getFactory(new ArrayList<>(), (SkysailServerResource<?>) new TestListOfEnumResource()),
                is(instanceOf(ListEnumFieldFactory.class)));
    }

    @Test
    public void formResponse_source_yields_SkysailResponseEntityFieldFactory() {
        TestEntity entity = new TestEntity();
        assertThat(FieldsFactory.getFactory(new FormResponse<TestEntity>(entity, "target"),
                (SkysailServerResource<?>) new TestListOfEnumResource()),
                is(instanceOf(SkysailResponseEntityFieldFactory.class)));
    }

    @Test
    public void other_sources_yield_ListEnumFieldFactory() {
        assertThat(
                FieldsFactory.getFactory(new TestEntity(), (SkysailServerResource<?>) new TestListOfEnumResource()),
                is(instanceOf(DefaultEntityFieldFactory.class)));
    }

}
