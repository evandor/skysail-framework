package io.skysail.server.app.snap;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.snap.SnapApplication;
import io.skysail.server.restlet.resources.EntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class ModelResource extends EntityServerResource<io.skysail.server.app.snap.Model> {

    private String id;
    private SnapApplication app;
    private ModelRepository repository;

    public ModelResource() {
        addToContext(ResourceContextId.LINK_TITLE, "details");
        addToContext(ResourceContextId.LINK_GLYPH, "search");
    }

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (SnapApplication) getApplication();
        repository = (ModelRepository) app.getRepository(io.skysail.server.app.snap.Model.class);
    }


    @Override
    public SkysailResponse<?> eraseEntity() {
        repository.delete(id);
        return new SkysailResponse<>();
    }

    @Override
    public io.skysail.server.app.snap.Model getEntity() {
        return (io.skysail.server.app.snap.Model)app.getRepository().findOne(id);
    }

	@Override
    public List<Link> getLinks() {
        return super.getLinks(PutModelResource.class);
    }

}