package io.skysail.server.app.designer.fields.resources.date;

import io.skysail.server.ResourceContextId;
import io.skysail.server.app.designer.fields.DbEntityTimeField;
import io.skysail.server.app.designer.fields.resources.PutFieldResource;

public class PutTimeFieldResource extends PutFieldResource<DbEntityTimeField> {

    public PutTimeFieldResource() {
        addToContext(ResourceContextId.LINK_TITLE, "update Time Field");
    }

}
