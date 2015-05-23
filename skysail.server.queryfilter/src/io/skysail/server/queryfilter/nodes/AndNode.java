package io.skysail.server.queryfilter.nodes;

import io.skysail.server.queryfilter.*;

import java.util.List;


public class AndNode extends BranchNode {

    public AndNode() {
        super(Operation.AND);
    }

    public AndNode(List<ExprNode> childList) {
        super(Operation.AND, childList);
    }

    public Object getChildren() {
        return null;
    }

}
