package io.skysail.server.app.designer.fields.resources.text;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.server.app.designer.fields.DbEntityTextField;
import io.skysail.server.app.designer.fields.resources.PostFieldResource;

public class PostTextFieldResource extends PostFieldResource<DbEntityTextField> {

    public PostTextFieldResource() {
        super(PostTextFieldResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "new Text Field");
    }

    @Override
    public DbEntityTextField createEntityTemplate() {
        return new DbEntityTextField("", false);
    }


}
