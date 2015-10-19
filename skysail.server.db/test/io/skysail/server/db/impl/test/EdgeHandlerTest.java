package io.skysail.server.db.impl.test;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.*;
import io.skysail.server.db.impl.EdgeHandler;

import java.util.*;

import lombok.Data;

import org.junit.*;
import org.mockito.Mockito;

import com.tinkerpop.blueprints.*;
import com.tinkerpop.blueprints.impls.orient.*;

public class EdgeHandlerTest {

    @Data
    public class SomeUser implements Identifiable {
        private String id;
        @Field(inputType = InputType.MULTISELECT)
        private List<SomeRole> roles = new ArrayList<>();
        private String roleId;
    }

    @Data
    public class SomeRole implements Identifiable {
        private String id;
        @Field(inputType = InputType.TEXT)
        private String name;
    }

    private OrientGraph db;
    private SomeUser theUser;
    private SomeRole theRole;
    private OrientVertex vertex;
    private EdgeHandler edgeHandler;

    @Before
    public void setUp() throws Exception {
        vertex = Mockito.mock(OrientVertex.class);
        db = Mockito.mock(OrientGraph.class);
        theUser = new SomeUser();
        theRole = new SomeRole();
        theUser.getRoles().add(theRole);
        edgeHandler = new EdgeHandler(null,db);
    }

    @Test
    public void new_vertex_with_new_edge_is_added_to_collection_field_for_reference_without_id() throws Exception {
        theRole.setId(null);

        edgeHandler.handleEdges(theUser, vertex, Collections.emptyMap(), "roles");

        Mockito.verify(db).addVertex(theRole);
        Mockito.verify(db).addEdge(null, vertex, null, "roles");
    }

    @Test
    public void existing_vertex_with_new_edge_is_added_to_collection_field_for_reference_with_id() throws Exception {
        theRole.setId("theRoleId");

        edgeHandler.handleEdges(theUser, vertex, Collections.emptyMap(), "roles");

        Mockito.verify(db).getVertex(theRole.getId());
        Mockito.verify(db).addEdge(null, vertex, null, "roles");
    }

    @Test
    public void id_field_is_added_as_edge_to_provided_vertex() throws Exception {
        Edge edge = Mockito.mock(Edge.class);
        Iterable<Edge> edges = Arrays.asList(edge);
        Mockito.when(vertex.getEdges(Direction.OUT, "roleId")).thenReturn(edges);
        Mockito.when(db.getVertex(edge.toString())).thenReturn(vertex);
//        edgeHandler.handleEdges(theUser, vertex, Collections.emptyMap(), "roleId");
//        Mockito.verify(db).removeEdge(edge);
//        Mockito.verify(db).addEdge(null, vertex, null, "roleId");
    }
}
