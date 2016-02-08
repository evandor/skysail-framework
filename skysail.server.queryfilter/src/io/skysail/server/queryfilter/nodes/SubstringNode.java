package io.skysail.server.queryfilter.nodes;

import io.skysail.server.queryfilter.Operation;
import lombok.ToString;

@ToString
public class SubstringNode extends LeafNode {

    public SubstringNode(String attribute, String value) {
        super(Operation.SUBSTRING, attribute, value);
    }

}
