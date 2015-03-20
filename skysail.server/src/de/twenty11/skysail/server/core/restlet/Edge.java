package de.twenty11.skysail.server.core.restlet;

import io.skysail.api.links.Link;

public class Edge {

    //private Node from;
    private Node to;
    private Link linkheader;

    public Edge(Node node, Link linkheader) {
      //  this.from = node;
        this.linkheader = linkheader;
    }
    
//    public Node getFrom() {
//        return from;
//    }
    
    public Link getLinkheader() {
        return linkheader;
    }

    public Node getTo() {
        return to;
    }

    public void setTo(Node to) {
        this.to = to;
    }
    
    @Override
    public String toString() {
        return new StringBuilder(linkheader.toString()).append(": ").append(" -> ").append(to != null ? to.getPath() : "<null>").toString();
    }

}
