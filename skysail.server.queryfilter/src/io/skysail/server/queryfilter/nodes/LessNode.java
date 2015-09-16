package io.skysail.server.queryfilter.nodes;

import io.skysail.server.queryfilter.Operation;
import lombok.ToString;

@ToString(callSuper = true)
public class LessNode extends LeafNode {

    public LessNode(String attribute, String value) {
        super(Operation.LESS, attribute, value);
    }

}
