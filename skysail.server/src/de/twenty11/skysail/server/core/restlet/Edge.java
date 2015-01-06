package de.twenty11.skysail.server.core.restlet;

import de.twenty11.skysail.api.responses.Linkheader;

public class Edge {

    //private Node from;
    private Node to;
    private Linkheader linkheader;

    public Edge(Node node, Linkheader linkheader) {
      //  this.from = node;
        this.linkheader = linkheader;
    }
    
//    public Node getFrom() {
//        return from;
//    }
    
    public Linkheader getLinkheader() {
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
