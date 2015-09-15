package io.skysail.server.queryfilter.nodes;

import io.skysail.server.queryfilter.Operation;
import lombok.ToString;

@ToString(callSuper = true)
public class GreaterNode extends LeafNode {

    public GreaterNode(String attribute, String value) {
        super(Operation.GREATER, attribute, value);
    }

}
