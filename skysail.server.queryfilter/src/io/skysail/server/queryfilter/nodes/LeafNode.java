package io.skysail.server.queryfilter.nodes;

import io.skysail.server.queryfilter.Operation;
import lombok.*;

@Getter
@Setter
@ToString
public abstract class LeafNode extends AbstractExprNode {

    protected String attribute;
    private String value;

    protected LeafNode(Operation op) {
        super(op);
    }

    protected LeafNode(String attribute, Operation op) {
        super(op);
        this.attribute = attribute;
    }

    public LeafNode(Operation op, String attribute, String value) {
        super(op);
        this.attribute = attribute;
        this.value = value != null ? value.trim() : "";
    }

    public final boolean isLeaf() {
        return true;
    }



}
