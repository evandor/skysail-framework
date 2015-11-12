package io.skysail.server.codegen.model.entities;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import io.skysail.server.codegen.model.entities.*;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class EntityGraphTest {

	private HashSet<AptEntity> nodes;
	private HashSet<Reference> edges;
	private AptEntity n1, n2;

	@Before
	public void setUp() throws Exception {
		n1 = new AptEntity("p", "n1");
        n2 = new AptEntity("p", "n2");
		nodes = new HashSet<>();
		edges = new HashSet<>();
		nodes.add(n1);
		nodes.add(n2);
		edges.add(new Reference(n1, n2));
	}

	@Test
	public void empty_graph_has_no_nodes_with_no_incoming_edges() throws Exception {
		EntityGraph graph = new EntityGraph(new HashSet<>(), new HashSet<>());
		assertThat(graph.getNodesWithNoIncomingEdge().size(), is(0));
	}

	@Test
	public void incomingEdge_is_detected() throws Exception {
		EntityGraph graph = new EntityGraph(nodes, edges);
		assertThat(graph.getNodesWithNoIncomingEdge().size(), is(1));
		assertThat(graph.getNodesWithNoIncomingEdge().get(0), is(equalTo(n1)));
	}

	   @Test
	    public void outgoingEdge_is_detected() throws Exception {
	        EntityGraph graph = new EntityGraph(nodes, edges);
	        assertThat(graph.getOutgoingEdges(n1).size(), is(1));
            assertThat(graph.getOutgoingEdges(n2).size(), is(0));
	    }

	@Test
	public void testName() throws Exception {
		EntityGraph graph = new EntityGraph(nodes, edges);
		assertThat(graph.getIncomingEdges(n1).size(), is(0));
		assertThat(graph.getIncomingEdges(n2).size(), is(1));
	}
	
	@Test
    public void finds_node_by_packageName_and_simpleName() {
        EntityGraph graph = new EntityGraph(nodes, edges);
        assertThat(graph.getNode("p", "n1"), is(equalTo(n1)));
    }

    @Test
    public void gets_all_nodes() {
        EntityGraph graph = new EntityGraph(nodes, edges);
        assertThat(graph.getNodes().size(), is(2));
    }


}
