package io.skysail.server.model.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.server.model.DefaultListFieldFactory;

import java.util.List;

import lombok.SneakyThrows;

import org.junit.*;

import de.twenty11.skysail.server.core.FormField;

@Ignore
public class DefaultListFieldFactoryTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    @SneakyThrows
    public void testName() {
        TestEntity source = new TestEntity();
        List<FormField> formfields = new DefaultListFieldFactory(source).determineFrom(new TestListResource(), null);
        assertThat(formfields.size(), is(2));
        assertThat(formfields.get(0).getName(), is(equalTo("stringField")));
        assertThat(formfields.get(1).getName(), is(equalTo("stringField_Textarea")));
    }
}
