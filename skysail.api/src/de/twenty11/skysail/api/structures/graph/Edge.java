package de.twenty11.skysail.api.structures.graph;

import org.apache.commons.lang.Validate;

public class Edge {

    private Node to;
    private Node from;
    private String ident = null;

    public Edge(Node nodeA, Node nodeB) {
        Validate.notNull(nodeA, "edge from nodeA to nodeB does not accept null value for nodeA");
        Validate.notNull(nodeA, "edge from nodeA to nodeB does not accept null value for nodeB");
        this.from = nodeA;
        this.to = nodeB;
    }
    
    public Edge(Node nodeA, Node nodeB, String ident) {
        this(nodeA,nodeB);
        this.ident = ident;
    }

    public Node getFrom() {
        return from;
    }
    
    public Node getTo() {
        return to;
    }
    
    public String getIdent() {
        return ident;
    }

}
