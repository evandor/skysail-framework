package de.twenty11.skysail.api.structures.graph;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

public class Graph {

    private Set<Node> nodes = Collections.newSetFromMap(new ConcurrentHashMap<>());

    private List<Edge> edges = new CopyOnWriteArrayList<>();

    private String ident;

    public Graph(String ident) {
        this.ident = ident;
    }

    public Stream<Edge> getEdges() {
        return edges.stream();
    }

    public Stream<Node> getNodes() {
        return nodes.stream();
    }

    public String getIdent() {
        return ident;
    }

    public void add(Node node) {
        if (nodes.contains(node)) {
            throw new IllegalArgumentException("Node '" + node.toString() + "' already exists. Consider using #replace");
        }
        nodes.add(node);
    }

    public void replace(Node node) {
        nodes.add(node);
    }

    public void delete(Node node) {
        nodes.remove(node);
    }

    /**
     * @param edge
     *            the edge
     */
    public synchronized void add(Edge edge) {
        if (!nodes.contains(edge.getFrom())) {
            throw new IllegalArgumentException("Edge '" + edge.toString() + "' uses an unknown 'from' node. Add node '"
                    + edge.getFrom().getIdent() + "' to the graph first.");
        }
        if (!nodes.contains(edge.getTo())) {
            throw new IllegalArgumentException("Edge '" + edge.toString() + "' uses an unknown 'to' node. Add node '"
                    + edge.getTo().getIdent() + "' to the graph first.");
        }
        edges.add(edge);
    }

    public synchronized void replace(Edge oldEdge, Edge newEdge) {
        edges.remove(oldEdge);
        edges.add(newEdge);
    }

    public void remove(Edge edge) {
        edges.remove(edge);
    }

}