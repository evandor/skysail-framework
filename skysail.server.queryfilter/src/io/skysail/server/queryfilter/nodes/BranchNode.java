package io.skysail.server.queryfilter.nodes;

import io.skysail.server.queryfilter.*;

import java.util.List;

import lombok.*;

@ToString
public abstract class BranchNode extends AbstractExprNode {

    @Getter
    protected List<ExprNode> childList;

    protected BranchNode(Operation op) {
        super(op);
    }

    public BranchNode(Operation op, List<ExprNode> childList) {
        super(op);
        this.childList = childList;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

}
