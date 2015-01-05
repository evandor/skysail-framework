package de.twenty11.skysail.api.structures.graph;

import java.util.Objects;

public class Node {

    private String ident;

    public Node(String ident) {
        this.ident = ident;
    }
    
    public String getIdent() {
        return ident;
    }
    
    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof Node)) {
            return false;
        }
        return Objects.equals(this.ident, ((Node)other).getIdent());
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.ident);
    }

}
