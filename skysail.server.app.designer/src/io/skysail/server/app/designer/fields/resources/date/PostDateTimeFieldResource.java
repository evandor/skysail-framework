package io.skysail.server.app.designer.fields.resources.date;

import io.skysail.server.ResourceContextId;
import io.skysail.server.app.designer.fields.DbEntityDateTimeField;
import io.skysail.server.app.designer.fields.resources.PostFieldResource;

public class PostDateTimeFieldResource extends PostFieldResource<DbEntityDateTimeField> {

    public PostDateTimeFieldResource() {
        super(PostDateTimeFieldResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "new DateTime");
    }
    
    @Override
    public DbEntityDateTimeField createEntityTemplate() {
        return new DbEntityDateTimeField("", false);
    }


}
