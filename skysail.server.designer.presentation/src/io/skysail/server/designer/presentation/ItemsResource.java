package io.skysail.server.designer.presentation;

import io.skysail.server.queryfilter.Filter;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.api.links.Link;

import java.util.List;
import java.util.Map;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class ItemsResource extends ListServerResource<io.skysail.server.designer.presentation.Item> {

    private PresentationApplication app;
    private ItemRepository repository;

    public ItemsResource() {
        super(ItemResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list Items");
    }

    @Override
    protected void doInit() {
        app = (PresentationApplication) getApplication();
        repository = (ItemRepository) app.getRepository(io.skysail.server.designer.presentation.Item.class);
    }

    @Override
    public List<io.skysail.server.designer.presentation.Item> getEntity() {
       return repository.find(new Filter(getRequest()));
    }

    public List<Link> getLinks() {
       return super.getLinks(PostItemResource.class);
    }
}