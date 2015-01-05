package de.twenty11.skysail.api.structures.graph.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import de.twenty11.skysail.api.structures.graph.Edge;
import de.twenty11.skysail.api.structures.graph.Graph;
import de.twenty11.skysail.api.structures.graph.Node;

public class GraphTest {

    private Graph graph;
    private Node nodeA;
    private Node nodeB;

    @Before
    public void setUp() throws Exception {
        graph = new Graph("ident");
        nodeA = new Node("identA");
        nodeB = new Node("identB");
    }

    @Test
    public void can_be_empty() {
        assertThat(graph.getNodes().count(), is(0L));
        assertThat(graph.getEdges().count(), is(0L));
    }
    
    @Test
    public void accepts_new_node() {
        graph.add(new Node("ident"));
        assertThat(graph.getNodes().count(), is(1L));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void does_not_accept_adding_an_existing_node() {
        graph.add(new Node("ident"));
        graph.add(new Node("ident"));
    }

    @Test
    public void replaces_an_existing_node() {
        graph.add(new Node("ident"));
        graph.replace(new Node("ident"));
        assertThat(graph.getNodes().count(), is(1L));
    }

    @Test
    public void deletes_node_by_identifier() {
        graph.add(new Node("ident"));
        assertThat(graph.getNodes().count(), is(1L));
        graph.delete(new Node("ident"));
        assertThat(graph.getNodes().count(), is(0L));
    }

    @Test(expected = IllegalArgumentException.class)
    public void does_not_accept_adding_an_edge_with_unknown_target() {
        graph.add(nodeA);
        graph.add(new Edge(nodeA,nodeB));
    }

    @Test(expected = IllegalArgumentException.class)
    public void does_not_accept_adding_an_edge_with_unknown_sink() {
        graph.add(nodeB);
        graph.add(new Edge(nodeA,nodeB));
    }
    
    @Test
    public void adds_new_edge_with_identical_sink_and_target() throws Exception {
        graph.add(nodeA);
        graph.add(new Edge(nodeA, nodeA));
        assertThat(graph.getNodes().count(), is(1L));
        assertThat(graph.getEdges().count(), is(1L));
    }

    @Test
    public void adds_two_edges_to_the_same_nodes() throws Exception {
        graph.add(nodeA);
        graph.add(nodeB);
        graph.add(new Edge(nodeA, nodeB));
        graph.add(new Edge(nodeA, nodeB));
        assertThat(graph.getNodes().count(), is(2L));
        assertThat(graph.getEdges().count(), is(2L));
    }

    @Test
    public void adds_edge_with_provided_identifier() {
        graph.add(nodeA);
        graph.add(nodeB);
        Edge edgeAB_first = new Edge(nodeA, nodeB, "first");
        graph.add(edgeAB_first);
        assertThat(graph.getEdges().findFirst().get().getIdent(), is(equalTo("first")));
    }
    
    @Test
    public void replaces_edge_with_new_one() {
        graph.add(nodeA);
        graph.add(nodeB);
        Edge edgeAB_first = new Edge(nodeA, nodeB, "first");
        graph.add(edgeAB_first);

        Edge edgeAB_second = new Edge(nodeA, nodeB, "second");
        graph.replace(edgeAB_first, edgeAB_second);
        
        assertThat(graph.getNodes().count(), is(2L));
        assertThat(graph.getEdges().count(), is(1L));
        assertThat(graph.getEdges().findFirst().get().getIdent(), is(equalTo("second")));
    }
    
    @Test
    public void removes_edge() throws Exception {
        graph.add(nodeA);
        graph.add(nodeB);
        Edge edgeAB = new Edge(nodeA, nodeB, "first");
        graph.add(edgeAB);

        graph.remove(edgeAB);
        
        assertThat(graph.getNodes().count(), is(2L));
        assertThat(graph.getEdges().count(), is(0L));
    }

}
