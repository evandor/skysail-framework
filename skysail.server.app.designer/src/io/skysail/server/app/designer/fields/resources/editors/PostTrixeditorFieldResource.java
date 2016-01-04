package io.skysail.server.app.designer.fields.resources.editors;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.server.app.designer.fields.DbEntityTrixeditorField;
import io.skysail.server.app.designer.fields.resources.PostFieldResource;

public class PostTrixeditorFieldResource extends PostFieldResource<DbEntityTrixeditorField> {

    public PostTrixeditorFieldResource() {
        addToContext(ResourceContextId.LINK_TITLE, "new HtmlEditor (Trix)");
    }

    @Override
    public DbEntityTrixeditorField createEntityTemplate() {
        return new DbEntityTrixeditorField("", false);
    }


}
