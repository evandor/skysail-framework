package io.skysail.server.queryfilter.nodes;

import io.skysail.server.queryfilter.Operation;
import lombok.ToString;

@ToString
public class EqualityNode extends LeafNode {

    public EqualityNode(String attribute, String value) {
        super(Operation.EQUAL, attribute, value);
    }

}
