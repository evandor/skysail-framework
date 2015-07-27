package io.skysail.server.queryfilter.nodes;

import io.skysail.server.queryfilter.Operation;

public class IsInNode extends LeafNode {

    public IsInNode(String attribute, String value) {
        super(Operation.IN, attribute, value);
    }

}
