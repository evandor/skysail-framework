package io.skysail.server.queryfilter;

public interface ExprNode {

    boolean isLeaf();

    Operation getOperation();

    Object accept( FilterVisitor visitor );

}
