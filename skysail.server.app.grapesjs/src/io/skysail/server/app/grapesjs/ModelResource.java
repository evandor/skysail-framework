package io.skysail.server.app.grapesjs;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.grapesjs.GrapesJsApplication;
import io.skysail.server.restlet.resources.EntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class ModelResource extends EntityServerResource<io.skysail.server.app.grapesjs.Model> {

    private String id;
    private GrapesJsApplication app;
    private ModelRepository repository;

    public ModelResource() {
        addToContext(ResourceContextId.LINK_TITLE, "details");
        addToContext(ResourceContextId.LINK_GLYPH, "search");
    }

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (GrapesJsApplication) getApplication();
        repository = (ModelRepository) app.getRepository(io.skysail.server.app.grapesjs.Model.class);
    }


    @Override
    public SkysailResponse<?> eraseEntity() {
        repository.delete(id);
        return new SkysailResponse<>();
    }

    @Override
    public io.skysail.server.app.grapesjs.Model getEntity() {
        return (io.skysail.server.app.grapesjs.Model)app.getRepository().findOne(id);
    }

	@Override
    public List<Link> getLinks() {
        return super.getLinks(PutModelResource.class);
    }

}