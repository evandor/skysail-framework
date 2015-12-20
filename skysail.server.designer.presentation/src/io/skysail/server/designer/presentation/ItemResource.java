package io.skysail.server.designer.presentation;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class ItemResource extends EntityServerResource<io.skysail.server.designer.presentation.Item> {

    private String id;
    private PresentationApplication app;
    private ItemRepository repository;

    public ItemResource() {
        addToContext(ResourceContextId.LINK_TITLE, "details");
        addToContext(ResourceContextId.LINK_GLYPH, "search");
    }

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (PresentationApplication) getApplication();
        repository = (ItemRepository) app.getRepository(io.skysail.server.designer.presentation.Item.class);
    }


    @Override
    public SkysailResponse<?> eraseEntity() {
        repository.delete(id);
        return new SkysailResponse<>();
    }

    @Override
    public io.skysail.server.designer.presentation.Item getEntity() {
        return (io.skysail.server.designer.presentation.Item)app.getRepository().findOne(id);
    }

	@Override
    public List<Link> getLinks() {
        return super.getLinks(PutItemResource.class);
    }

}