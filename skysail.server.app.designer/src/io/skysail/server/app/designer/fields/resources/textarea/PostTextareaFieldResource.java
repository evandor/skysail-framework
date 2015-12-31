package io.skysail.server.app.designer.fields.resources.textarea;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.server.app.designer.fields.DbEntityTextareaField;
import io.skysail.server.app.designer.fields.resources.PostFieldResource;

public class PostTextareaFieldResource extends PostFieldResource<DbEntityTextareaField> {

    public PostTextareaFieldResource() {
        addToContext(ResourceContextId.LINK_TITLE, "new Textarea");
    }

    @Override
    public DbEntityTextareaField createEntityTemplate() {
        return new DbEntityTextareaField("", false);
    }


}
