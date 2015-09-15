package io.skysail.server.queryfilter.nodes;

import io.skysail.server.queryfilter.*;

import java.util.List;

import lombok.ToString;

@ToString
public class OrNode extends BranchNode {

    public OrNode() {
        super(Operation.OR);
    }

    public OrNode(List<ExprNode> childList) {
        super(Operation.OR, childList);
    }

    public Object getChildren() {
        return null;
    }

}
