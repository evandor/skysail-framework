package io.skysail.server.app.designer.fields.resources.date;

import io.skysail.server.ResourceContextId;
import io.skysail.server.app.designer.fields.DbEntityDateTimeField;
import io.skysail.server.app.designer.fields.resources.PutFieldResource;

public class PutDateTimeFieldResource extends PutFieldResource<DbEntityDateTimeField> {

    public PutDateTimeFieldResource() {
        addToContext(ResourceContextId.LINK_TITLE, "update DateTime Field");
    }

}
