package io.skysail.server.app.designer.fields.resources.url;

import io.skysail.server.ResourceContextId;
import io.skysail.server.app.designer.fields.DbEntityTrixeditorField;
import io.skysail.server.app.designer.fields.resources.PutFieldResource;

public class PutUrlFieldResource extends PutFieldResource<DbEntityTrixeditorField> {

    public PutUrlFieldResource() {
        addToContext(ResourceContextId.LINK_TITLE, "update Editor Field (Trix)");
    }

}
