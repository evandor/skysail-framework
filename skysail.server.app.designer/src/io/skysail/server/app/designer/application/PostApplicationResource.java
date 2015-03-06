package io.skysail.server.app.designer.application;

import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.PostEntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PostApplicationResource extends PostEntityServerResource<Application2> {

    public PostApplicationResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new Application2");
    }

    @Override
    public Application2 createEntityTemplate() {
        return new Application2();
    }

    @Override
    public SkysailResponse<?> addEntity(Application2 entity) {
        ApplicationsRepository.getInstance().add(entity);
        return new SkysailResponse<String>();
    }

}
