package io.skysail.server.queryfilter.nodes;

import io.skysail.server.queryfilter.Operation;
import lombok.ToString;

@ToString
public class PresentNode extends LeafNode {

    public PresentNode(String attribute, String value) {
        super(Operation.PRESENT, attribute, value);
    }

}
