package io.skysail.server.app.bpmnmodeler;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.EntityServerResource;

public class ModelResource extends EntityServerResource<io.skysail.server.app.bpmnmodeler.Model> {

    private String id;
    private BpmnModelerApplication app;
    private ModelRepository repository;

    public ModelResource() {
        addToContext(ResourceContextId.LINK_TITLE, "details");
        addToContext(ResourceContextId.LINK_GLYPH, "search");
    }

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (BpmnModelerApplication) getApplication();
        repository = (ModelRepository) app.getRepository(io.skysail.server.app.bpmnmodeler.Model.class);
    }


    @Override
    public SkysailResponse<?> eraseEntity() {
        repository.delete(id);
        return new SkysailResponse<>();
    }

    @Override
    public io.skysail.server.app.bpmnmodeler.Model getEntity() {
        return (io.skysail.server.app.bpmnmodeler.Model)app.getRepository().findOne(id);
    }

	@Override
    public List<Link> getLinks() {
        return super.getLinks(PutModelResource.class);
    }

}