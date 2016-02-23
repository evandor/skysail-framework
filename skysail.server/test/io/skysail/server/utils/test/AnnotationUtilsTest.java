package io.skysail.server.utils.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.util.*;

import org.junit.*;

import io.skysail.domain.html.*;
import io.skysail.server.utils.AnnotationUtils;
import lombok.Data;

public class AnnotationUtilsTest {

    @Data
    public class TestObject {
        @Field
        private String name;
        @Relation
        private List<String> relations;
    }

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testName() throws Exception {
        TestObject objectWithAnnotatedRelationField = new TestObject();
        objectWithAnnotatedRelationField.setName("theName");
        objectWithAnnotatedRelationField.setRelations(Arrays.asList("hi","there"));

        TestObject deserializableObject = (TestObject) AnnotationUtils.removeRelationData(objectWithAnnotatedRelationField);

        assertThat(deserializableObject.getName(),is("theName"));
        assertThat(deserializableObject.getRelations(),is(nullValue()));
        
    }
}
