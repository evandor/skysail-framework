package io.skysail.server.db.impl.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import io.skysail.server.db.impl.Persister;
import io.skysail.server.db.impl.test.entities.*;

import java.util.*;

import org.junit.*;
import org.mockito.Mockito;

import com.tinkerpop.blueprints.*;
import com.tinkerpop.blueprints.impls.orient.*;

public class PersisterTest {

    private static final String SOME_USER_ID = "#80:15";
    private static final String SOME_ROLE_ID = "#47:11";

    private OrientGraph db;
    private SomeUser userWithRole;
    private SomeRole theRole;
    private OrientVertex userVertex;
    private OrientVertex roleVertex, roleVertex2;
    private Persister persister;

    @Before
    public void setUp() throws Exception {
        db = Mockito.mock(OrientGraph.class);

        userVertex = mockVertexWithTitle("userVertex");
        roleVertex = mockVertexWithTitle("role1Vertex");
        roleVertex2 = mockVertexWithTitle("role2Vertex");

        userWithRole = new SomeUser("theUsername");
        theRole = new SomeRole("aRole");
        userWithRole.getRoles().add(theRole);

        persister = new Persister(db, new String[] { "roles" });

        Mockito.when(db.addVertex("class:SomeUser")).thenReturn(userVertex);
        Mockito.when(db.addVertex("class:SomeRole")).thenReturn(roleVertex);
        Mockito.when(db.getVertex(SOME_USER_ID)).thenReturn(userVertex);
        Mockito.when(db.getVertex(SOME_ROLE_ID)).thenReturn(roleVertex);

        Mockito.when(userVertex.getId()).thenReturn(SOME_USER_ID);
        Mockito.when(userVertex.getEdges(Direction.OUT, "roles")).thenReturn(new ArrayList<>());
    }

    @Test
    public void vertex_for_new_user_without_roles_is_created() {
        persister.persist(new SomeUser("theUsername"));
        verifyPropertyWasSet(userVertex, "username", "theUsername");
        verifyAddEdgeIsNotCalled();
    }

    @Test
    public void vertices_and_edge_for_new_user_with_role_with_null_id_is_created() {
        theRole.setId(null);

        Object persist = persister.persist(userWithRole);

        assertThat(persist, is(notNullValue()));
        verifyPropertyWasSet(userVertex, "username", "theUsername");
        verifyPropertyWasSet(roleVertex, "rolename", "aRole");
        verifyEdgeWasCreated(userVertex, roleVertex, "roles");
    }

    @Test
    public void vertice_and_edge_for_new_user_with_existing_role_is_created() {
        theRole.setId(SOME_ROLE_ID);

        persister.persist(userWithRole);

        verifyPropertyWasSet(userVertex, "username", "theUsername");
        verifyPropertyWasSet(roleVertex, "rolename", "aRole");
        verifyEdgeWasCreated(userVertex, roleVertex, "roles");
    }

    @Test
    public void existing_user_with_role_gets_a_new_role() throws Exception {
        SomeUser theUser = setupExistingUserWithRole();

        Mockito.when(db.addVertex("class:SomeRole")).thenReturn(roleVertex2);
        Edge edge = Mockito.mock(Edge.class);
        Mockito.when(edge.getLabel()).thenReturn("roles");
        Mockito.when(edge.getVertex(Direction.IN)).thenReturn(roleVertex);
        Mockito.when(edge.getVertex(Direction.OUT)).thenReturn(userVertex);
        Mockito.when(userVertex.getEdges(Direction.OUT, "roles")).thenReturn(Arrays.asList(edge));
        theUser.getRoles().add(new SomeRole("asecondrole"));
        persister.persist(theUser);

        verifyPropertyWasSet(userVertex, "username", "theUsername", 2);
        verifyPropertyWasSet(roleVertex, "rolename", "aRole", 2);
        verifyEdgeWasCreated(userVertex, roleVertex, "roles");
        verifyEdgeWasCreated(userVertex, roleVertex2, "roles");
    }

    private SomeUser setupExistingUserWithRole() {
        theRole.setId(null);
        Object id = persister.persist(userWithRole);
        userWithRole.setId((String) id);
        theRole.setId(SOME_ROLE_ID);
        return userWithRole;
    }

    private void verifyEdgeWasCreated(OrientVertex from, OrientVertex to, String name) {
        Mockito.verify(db).addEdge(null, from, to, name);
    }

    private void verifyPropertyWasSet(OrientVertex vertex, String key, String value) {
        Mockito.verify(vertex).setProperty(key, value);
    }

    private void verifyPropertyWasSet(OrientVertex vertex, String key, String value, int times) {
        Mockito.verify(vertex, Mockito.times(times)).setProperty(key, value);
    }

    private OrientEdge verifyAddEdgeIsNotCalled() {
        return Mockito.verify(db, org.mockito.Mockito.never()).addEdge(org.mockito.Mockito.any(Object.class),
                org.mockito.Mockito.any(Vertex.class), org.mockito.Mockito.any(Vertex.class),
                org.mockito.Mockito.anyString());
    }

    private OrientVertex mockVertexWithTitle(String title) {
        OrientVertex mock = Mockito.mock(OrientVertex.class);
        Mockito.when(mock.toString()).thenReturn(title);
        return mock;
    }
}
