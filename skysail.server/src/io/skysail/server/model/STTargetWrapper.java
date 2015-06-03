package io.skysail.server.model;

import io.skysail.server.app.SkysailApplication;
import lombok.Getter;

import org.restlet.representation.Variant;

public class STTargetWrapper {

    @Getter
    private Variant target;

    public STTargetWrapper(Variant target) {
        this.target = target;
    }

    public boolean isTreeForm() {
        return SkysailApplication.SKYSAIL_TREE_FORM.getName().equals(target.getMediaType().getName());
    }

    @Override
    public String toString() {
        return target.toString();
    }
}
