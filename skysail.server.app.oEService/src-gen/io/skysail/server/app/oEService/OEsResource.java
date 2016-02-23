package io.skysail.server.app.oEService;

import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.api.links.Link;

import java.util.*;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class OEsResource extends ListServerResource<io.skysail.server.app.oEService.OE> {

    private OEServiceApplication app;
    private OERepository repository;

    public OEsResource() {
        super(OEResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list OEs");
    }

    public OEsResource(Class<? extends OEResource> cls) {
        super(cls);
    }

    @Override
    protected void doInit() {
        app = (OEServiceApplication) getApplication();
        repository = (OERepository) app.getRepository(io.skysail.server.app.oEService.OE.class);
    }

    @Override
    public List<io.skysail.server.app.oEService.OE> getEntity() {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse(), repository.count(filter));
        return repository.find(filter, pagination);
    }

    public List<Link> getLinks() {
              return super.getLinks(PostOEResource.class,OEsResource.class,UsersResource.class);
    }
}