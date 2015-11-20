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
        return app.getSpacesRepo().findOne(id);
    }

    @Override
    public SkysailResponse<Space> updateEntity(Space entityFromTheWire) {
        Space entityToBeUpdated = getEntity(null);
        copyProperties(entityToBeUpdated,entityFromTheWire);
        app.getSpacesRepo().update(id, entityToBeUpdated);
        return new SkysailResponse<>(entityToBeUpdated);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(SpacesResource.class);
    }

}
