package io.skysail.server.app.wiki.pages;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.wiki.PostDynamicEntityServerResource;
import io.skysail.server.app.wiki.WikiApplication;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PostPageResource extends PostDynamicEntityServerResource<Page> {
    
    public PostPageResource() {
        addToContext(ResourceContextId.LINK_TITLE, "create new Page");
    }

    @Override
    public Page createEntityTemplate() {
        return new Page();
    }

    public SkysailResponse<?> addEntity(Page entity) {
        ((WikiApplication) getApplication()).getRepository().add(entity);
        return new SkysailResponse<String>();
    }

}
