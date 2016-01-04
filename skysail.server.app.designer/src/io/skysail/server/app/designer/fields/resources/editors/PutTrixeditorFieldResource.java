package io.skysail.server.app.designer.fields.resources.editors;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.server.app.designer.fields.DbEntityTrixeditorField;
import io.skysail.server.app.designer.fields.resources.PutFieldResource;

public class PutTrixeditorFieldResource extends PutFieldResource<DbEntityTrixeditorField> {

    public PutTrixeditorFieldResource() {
        addToContext(ResourceContextId.LINK_TITLE, "update Trixeditor Field");
    }

}
