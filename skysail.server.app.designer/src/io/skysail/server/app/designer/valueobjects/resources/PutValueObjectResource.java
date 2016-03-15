package io.skysail.server.app.designer.valueobjects.resources;

import io.skysail.server.app.designer.valueobjects.ValueObjectElement;
import io.skysail.server.restlet.resources.PutEntityServerResource;

public class PutValueObjectResource extends PutEntityServerResource<ValueObjectElement> {

    @Override
    public ValueObjectElement getEntity() {
        return new ValueObjectElement();
    }

}
