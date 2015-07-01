package io.skysail.server.app.wiki.spaces.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.wiki.WikiApplication;
import io.skysail.server.app.wiki.spaces.Space;
import io.skysail.server.restlet.resources.PutEntityServerResource;

public class PutSpaceResource extends PutEntityServerResource<Space> {

    private String id;
    private WikiApplication app;

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (WikiApplication) getApplication();
    }
    
    @Override
    public Space getEntity() {
        return app.getRepository().getSpaceById(id);
    }

    @Override
    public SkysailResponse<?> updateEntity(Space entity) {
        Space original = getEntity(null);
        original.setName(entity.getName());
        app.getRepository().update(id, original);
        return new SkysailResponse<>();
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(SpacesResource.class);
    }

}
