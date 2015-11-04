package io.skysail.server.utils.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import io.skysail.api.domain.Identifiable;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.utils.PathUtils;

import java.util.*;

import org.junit.*;
import org.mockito.Mockito;

public class PathUtilsTest {

    private class MyIdentifiable implements Identifiable {
        public void setId(String id) {}
        public String getId() {return "#42";}
    };

    private SkysailServerResource<?> resource;

    private MyIdentifiable identifiable;

    private Map<String, Object> attributes;

    @Before
    public void setUp() throws Exception {
        resource = Mockito.mock(SkysailServerResource.class);
        identifiable = new MyIdentifiable();
        attributes = new HashMap<>();
        Mockito.when(resource.getRequestAttributes()).thenReturn(attributes);
    }

    @Test
    public void ordinary_object_will_get_unknown_id() {
        Map<String, String> substitutions = PathUtils.getSubstitutions(new String(), resource.getRequestAttributes(), Collections.emptyList());
        assertThat(substitutions.size(),is(1));
        assertThat(substitutions.get("id"),is("UNKNOWN_ID"));
    }

    @Test
    public void identifiable_has_id_as_substitution_id() {
        Map<String, String> substitutions = PathUtils.getSubstitutions(identifiable, resource.getRequestAttributes(), Collections.emptyList());
        assertThat(substitutions.size(),is(1));
        assertThat(substitutions.get("id"),is("42"));
    }

    @Test
    public void testName() {
        attributes.put("id", "something else");
        attributes.put("name", "theName");
        Map<String, String> substitutions = PathUtils.getSubstitutions(identifiable, resource.getRequestAttributes(), Collections.emptyList());
        assertThat(substitutions.size(),is(2));
        assertThat(substitutions.get("id"),is("42"));
        assertThat(substitutions.get("name"),is("theName"));
    }
}
