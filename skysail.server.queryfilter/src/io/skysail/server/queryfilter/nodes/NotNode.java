package io.skysail.server.queryfilter.nodes;

import io.skysail.server.queryfilter.*;

import java.util.Arrays;

import lombok.ToString;

@ToString
public class NotNode extends BranchNode {

    public NotNode(ExprNode child) {
        super(Operation.NOT, Arrays.asList(child));
    }

    public ExprNode getChild() {
        return this.childList.get(0);
    }

}
