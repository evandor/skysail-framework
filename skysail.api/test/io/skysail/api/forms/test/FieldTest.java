package io.skysail.api.forms.test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import io.skysail.api.forms.Field;

import org.junit.Before;
import org.junit.Test;

// TODO
public class FieldTest {

    private java.lang.reflect.Field field;

    private class TestClassWithAnnotations {

        @Field
        public String something;
    }

    @Before
    public void setUp() throws Exception {
        TestClassWithAnnotations cut = new TestClassWithAnnotations();
        // java.lang.reflect.Field[] fields = cut.getClass().getFields();
        field = cut.getClass().getField("something");
    }

    @Test
    public void testTags() throws Exception {
        assertThat(field.getAnnotation(Field.class), is(not(nullValue())));
    }

    @Test
    public void testType() throws Exception {
        // throw new RuntimeException("not yet implemented");
    }

    @Test
    public void testTag() throws Exception {
        // throw new RuntimeException("not yet implemented");
    }

    @Test
    public void testName() throws Exception {
        // throw new RuntimeException("not yet implemented");
    }

    @Test
    public void testHtmlPolicy() throws Exception {
        // throw new RuntimeException("not yet implemented");
    }

}
