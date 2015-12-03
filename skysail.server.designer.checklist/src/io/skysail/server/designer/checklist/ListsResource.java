package io.skysail.server.designer.checklist;

import io.skysail.server.queryfilter.Filter;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.api.links.Link;

import java.util.List;
import java.util.Map;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class ListsResource extends ListServerResource<io.skysail.server.designer.checklist.List> {

    private ChecklistApplication app;
    private ListRepository repository;

    public ListsResource() {
        super(ListResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list Lists");
    }

    @Override
    protected void doInit() {
        app = (ChecklistApplication) getApplication();
        repository = (ListRepository) app.getRepository(io.skysail.server.designer.checklist.List.class);
    }

    @Override
    public List<io.skysail.server.designer.checklist.List> getEntity() {
       return repository.find(new Filter(getRequest()));
    }

    public List<Link> getLinks() {
       return super.getLinks(PostListResource.class);
    }
}