package io.skysail.server.app.designer.fields.resources.text;

import io.skysail.server.ResourceContextId;
import io.skysail.server.app.designer.fields.DbEntityTextField;
import io.skysail.server.app.designer.fields.resources.PutFieldResource;

public class PutTextFieldResource extends PutFieldResource<DbEntityTextField> {

    public PutTextFieldResource() {
        addToContext(ResourceContextId.LINK_TITLE, "update Text Field");
    }

}
