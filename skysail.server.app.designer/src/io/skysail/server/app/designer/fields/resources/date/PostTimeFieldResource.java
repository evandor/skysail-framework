package io.skysail.server.app.designer.fields.resources.date;

import io.skysail.server.ResourceContextId;
import io.skysail.server.app.designer.fields.DbEntityTimeField;
import io.skysail.server.app.designer.fields.resources.PostFieldResource;

public class PostTimeFieldResource extends PostFieldResource<DbEntityTimeField> {

    public PostTimeFieldResource() {
        super(PostTimeFieldResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "new Time");
    }
    
    @Override
    public DbEntityTimeField createEntityTemplate() {
        return new DbEntityTimeField("", false);
    }


}
