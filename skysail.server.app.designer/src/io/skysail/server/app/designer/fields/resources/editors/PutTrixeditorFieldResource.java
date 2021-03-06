package io.skysail.server.app.designer.fields.resources.editors;

import io.skysail.server.ResourceContextId;
import io.skysail.server.app.designer.fields.DbEntityTrixeditorField;
import io.skysail.server.app.designer.fields.resources.PutFieldResource;

public class PutTrixeditorFieldResource extends PutFieldResource<DbEntityTrixeditorField> {

    public PutTrixeditorFieldResource() {
        addToContext(ResourceContextId.LINK_TITLE, "update Editor Field (Trix)");
    }

}
