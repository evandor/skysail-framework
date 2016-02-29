package io.skysail.server.app.bpmnmodeler;

import io.skysail.server.ResourceContextId;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.api.links.Link;

import java.util.*;

public class ModelsResource extends ListServerResource<io.skysail.server.app.bpmnmodeler.Model> {

    private BpmnModelerApplication app;
    private ModelRepository repository;

    public ModelsResource() {
        super(ModelResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list Models");
    }

    public ModelsResource(Class<? extends ModelResource> cls) {
        super(cls);
    }

    @Override
    protected void doInit() {
        app = (BpmnModelerApplication) getApplication();
        repository = (ModelRepository) app.getRepository(io.skysail.server.app.bpmnmodeler.Model.class);
    }

    @Override
    public List<io.skysail.server.app.bpmnmodeler.Model> getEntity() {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse(), repository.count(filter));
        return repository.find(filter, pagination);
    }

    public List<Link> getLinks() {
              return super.getLinks(PostModelResource.class,ModelsResource.class);
    }
}