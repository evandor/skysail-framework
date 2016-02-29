package io.skysail.server.app.designer.fields.resources.text;

import io.skysail.server.ResourceContextId;
import io.skysail.server.app.designer.fields.*;
import io.skysail.server.app.designer.fields.resources.PostFieldResource;

public class PostTextFieldResource extends PostFieldResource<DbEntityTextField> {

    public PostTextFieldResource() {
        super(PostTextFieldResource.class, FieldRole.GUID, FieldRole.OWNER);
        addToContext(ResourceContextId.LINK_TITLE, "new Text Field");
    }

    @Override
    public DbEntityTextField createEntityTemplate() {
        return new DbEntityTextField("", false);
    }


}
