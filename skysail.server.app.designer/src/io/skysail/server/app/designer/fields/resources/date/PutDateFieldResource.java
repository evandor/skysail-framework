package io.skysail.server.app.designer.fields.resources.date;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.server.app.designer.fields.DbEntityDateField;
import io.skysail.server.app.designer.fields.resources.PutFieldResource;

public class PutDateFieldResource extends PutFieldResource<DbEntityDateField> {

    public PutDateFieldResource() {
        addToContext(ResourceContextId.LINK_TITLE, "update Date Field");
    }

}
