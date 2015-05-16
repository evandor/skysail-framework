package io.skysail.server.converter.impl.factories.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.converter.impl.factories.SkysailResponseEntityFieldFactory;
import io.skysail.server.restlet.resources.SkysailServerResource;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.twenty11.skysail.server.core.FormField;

public class SkysailResponseEntityFieldFactoryTest {

    private SkysailResponseEntityFieldFactory factory;

    private SkysailServerResource<?> resource;

    @Before
    public void setUp() throws Exception {
        resource = new TestSkysailServerResource();
    }

    @Test
    public void test() throws Exception {
        SkysailResponse<?> source = new SkysailResponse<String>("entity");
        factory = new SkysailResponseEntityFieldFactory(source, TestEntity.class);
        List<FormField> fields = factory.determineFrom(resource);
        assertThat(fields.size(), is(1));
        assertThat(fields.get(0).getName(), is(equalTo("title")));
        assertThat(fields.get(0).getNameKey(), is(equalTo("java.lang.String.title")));
        assertThat(fields.get(0).getEntity(), is("entity"));
        assertThat(fields.get(0).getValue(), is(nullValue()));
        assertThat(fields.get(0).getInputType(), is(equalTo("text")));
    }

}
