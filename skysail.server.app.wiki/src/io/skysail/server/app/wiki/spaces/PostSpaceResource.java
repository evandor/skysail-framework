package io.skysail.server.app.wiki.spaces;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.wiki.PostDynamicEntityServerResource;
import io.skysail.server.app.wiki.WikiApplication;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PostSpaceResource extends PostDynamicEntityServerResource<Space> {

    public PostSpaceResource() {
        addToContext(ResourceContextId.LINK_TITLE, "create new Space");
    }

    @Override
    public Space createEntityTemplate() {
        return new Space();
    }

    public SkysailResponse<?> addEntity(Space entity) {
        ((WikiApplication) getApplication()).getRepository().add(entity);
        return new SkysailResponse<String>();
    }

}
