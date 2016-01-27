package io.skysail.server.app.designer.fields.resources.date;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.server.app.designer.fields.DbEntityDateField;
import io.skysail.server.app.designer.fields.resources.PostFieldResource;

public class PostDateFieldResource extends PostFieldResource<DbEntityDateField> {

    public PostDateFieldResource() {
        addToContext(ResourceContextId.LINK_TITLE, "new Date");
    }

    @Override
    public DbEntityDateField createEntityTemplate() {
        return new DbEntityDateField("", false);
    }


}
