package io.skysail.server.utils.test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import io.skysail.api.domain.Identifiable;
import io.skysail.server.utils.PathUtils;

import java.util.*;

import org.junit.*;
import org.mockito.Mockito;

import de.twenty11.skysail.server.core.restlet.RouteBuilder;

public class PathUtilsTest {

    private class MyIdentifiable implements Identifiable {
        public void setId(String id) {}
        public String getId() {return "#42";}
    };

    private MyIdentifiable identifiable;

    private Map<String, Object> requestAttributes;

    private List<RouteBuilder> routeBuilders;

    private PathUtils pathUtils;

    @Before
    public void setUp() throws Exception {
        identifiable = new MyIdentifiable();
        requestAttributes = new HashMap<>();
        routeBuilders = new ArrayList<>();
        pathUtils = new PathUtils(requestAttributes, routeBuilders);
    }

    @Test
    public void testName() {
        Map<String, String> substitutions = pathUtils.getSubstitutions("a string");
        assertThat(substitutions.size(),is(0));
    }

    @Test
    public void id_from_requestAttributes_is_added_to_substitutions_for_non_identifiable() {
        requestAttributes.put("id", "#15:5");
        Map<String, String> substitutions = pathUtils.getSubstitutions("a string");
        assertThat(substitutions.size(),is(1));
        assertThat(substitutions.get("id"),is("15:5"));
    }

    @Test
    public void requestattributes_are_added_to_substitutions1() {
        requestAttributes.put("id", "#15:5");
        Map<String, String> substitutions = pathUtils.getSubstitutions(identifiable);
        assertThat(substitutions.size(),is(1));
        assertThat(substitutions.get("id"),is("42"));
    }

    @Test
    public void testName2() {
        requestAttributes.put("id", "#47:11");
        RouteBuilder routeBuilder = mock(RouteBuilder.class);
        List<String> pathVariables = Arrays.asList("id", "todoId");
        Mockito.when(routeBuilder.getPathVariables()).thenReturn(pathVariables);
        routeBuilders.add(routeBuilder);
        Map<String, String> substitutions = pathUtils.getSubstitutions(identifiable);
        assertThat(substitutions.size(),is(2));
        assertThat(substitutions.get("id"),is("47:11"));
        assertThat(substitutions.get("todoId"),is("42"));

    }

}
