package io.skysail.server.utils.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.*;

import org.junit.*;
import org.mockito.Mockito;

import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import io.skysail.domain.Identifiable;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.utils.PathSubstitutions;

public class PathSubstitutionsTest {

    private class MyIdentifiable implements Identifiable {
        public void setId(String id) {}
        public String getId() {return "#42";}
    };

    private SkysailServerResource<?> resource;

    private MyIdentifiable identifiable;

    private Map<String, Object> attributes;

    private List<RouteBuilder> routeBuilders;

    @Before
    public void setUp() throws Exception {
        resource = Mockito.mock(SkysailServerResource.class);
        identifiable = new MyIdentifiable();
        attributes = new HashMap<>();
        Mockito.when(resource.getRequestAttributes()).thenReturn(attributes);
        routeBuilders = new ArrayList<>();
    }

    @Test
    public void ordinary_object_will_get_unknown_id() {
        PathSubstitutions pathUtils = new PathSubstitutions(attributes, routeBuilders);

        Map<String, String> substitutions = pathUtils.getFor(new String());
        
        assertThat(substitutions.size(),is(0));
    }

    @Test
    public void identifiable_has_id_as_substitution_id() {
        PathSubstitutions pathUtils = new PathSubstitutions(attributes, routeBuilders);

        Map<String, String> substitutions = pathUtils.getFor(identifiable);
        
        assertThat(substitutions.size(),is(1));
        assertThat(substitutions.get("id"),is("42"));
    }

    @Test
    public void substitutes_from_attributes() {
        attributes.put("id", "something else");
        attributes.put("name", "theName");
        
        Map<String, String> substitutions = new PathSubstitutions(attributes, routeBuilders).getFor(identifiable);
        
        assertThat(substitutions.size(),is(2));
        assertThat(substitutions.get("id"),is("42"));
        assertThat(substitutions.get("name"),is("theName"));
    }

    @Test
    public void substitutes_from_attributes_with_id_as_missing_pathvariable() {
        attributes.put("name", "theName");
        List<RouteBuilder> routes = new ArrayList<>();
        RouteBuilder routeBuilder = Mockito.mock(RouteBuilder.class);
        routes.add(routeBuilder);
        Mockito.when(routeBuilder.getPathVariables()).thenReturn(Arrays.asList("name", "connectionId"));

        Map<String, String> substitutions = new PathSubstitutions(attributes, routeBuilders).getFor(identifiable);
        
        assertThat(substitutions.size(),is(2));
        assertThat(substitutions.get("connectionId"),is("42"));
        assertThat(substitutions.get("name"),is("theName"));
    }
}
