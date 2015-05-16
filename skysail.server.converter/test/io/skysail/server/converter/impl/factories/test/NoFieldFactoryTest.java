package io.skysail.server.converter.impl.factories.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.server.converter.impl.factories.NoFieldFactory;
import io.skysail.server.restlet.resources.SkysailServerResource;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.twenty11.skysail.server.core.FormField;

public class NoFieldFactoryTest {

    private SkysailServerResource<?> resource;

    @Before
    public void setUp() throws Exception {
        resource = new TestSkysailServerResource();
    }
    
    @Test
    public void entity_with_no_properties_yields_empty_fields() throws Exception {
        NoFieldFactory factory = new NoFieldFactory();
        List<FormField> fields = factory.determineFrom(resource);
        assertThat(fields.size(), is(0));
    }
    

}
