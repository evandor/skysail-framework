package io.skysail.server.app.grapesjs;

import io.skysail.server.ResourceContextId;
import io.skysail.server.app.grapesjs.GrapesJsApplication;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.api.links.Link;

import java.util.*;

public class ModelsResource extends ListServerResource<io.skysail.server.app.grapesjs.Model> {

    private GrapesJsApplication app;
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
        app = (GrapesJsApplication) getApplication();
        repository = (ModelRepository) app.getRepository(io.skysail.server.app.grapesjs.Model.class);
    }

    @Override
    public List<io.skysail.server.app.grapesjs.Model> getEntity() {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse(), repository.count(filter));
        return repository.find(filter, pagination);
    }

    public List<Link> getLinks() {
              return super.getLinks(PostModelResource.class,ModelsResource.class);
    }
}