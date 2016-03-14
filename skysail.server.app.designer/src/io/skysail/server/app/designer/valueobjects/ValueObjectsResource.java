package io.skysail.server.app.designer.valueobjects;

import java.util.Collections;
import java.util.List;

import io.skysail.server.restlet.resources.ListServerResource;

public class ValueObjectsResource extends ListServerResource<ValueObject> {

    @Override
    public List<?> getEntity() {
        return Collections.emptyList();
    }

}
