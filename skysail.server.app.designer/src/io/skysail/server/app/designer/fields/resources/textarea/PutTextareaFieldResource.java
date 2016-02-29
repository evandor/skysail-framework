package io.skysail.server.app.designer.fields.resources.textarea;

import io.skysail.server.ResourceContextId;
import io.skysail.server.app.designer.fields.DbEntityTextareaField;
import io.skysail.server.app.designer.fields.resources.PutFieldResource;

public class PutTextareaFieldResource extends PutFieldResource<DbEntityTextareaField> {

    public PutTextareaFieldResource() {
        addToContext(ResourceContextId.LINK_TITLE, "update Text Field");
    }

}
