package io.skysail.server.app.designer.fields.resources.url;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.server.app.designer.fields.DbEntityUrlField;
import io.skysail.server.app.designer.fields.resources.PostFieldResource;

public class PostUrlFieldResource extends PostFieldResource<DbEntityUrlField> {

    public PostUrlFieldResource() {
        super(PostUrlFieldResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "new URL");
    }

    @Override
    public DbEntityUrlField createEntityTemplate() {
        return new DbEntityUrlField("", false);
    }


}
