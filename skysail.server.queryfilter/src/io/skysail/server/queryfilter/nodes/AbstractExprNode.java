package io.skysail.server.queryfilter.nodes;

import io.skysail.server.queryfilter.*;

public abstract class AbstractExprNode implements ExprNode {

    private Operation operation;

    public AbstractExprNode(Operation op) {
        this.operation = op;
    }
    
    @Override
    public final Operation getOperation() {
        return this.operation;
    }

    public final Object accept(FilterVisitor visitor) {
        return visitor.visit(this);
    }
}
