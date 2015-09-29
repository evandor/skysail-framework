package io.skysail.server.app.bb;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.restlet.resource.ResourceException;

import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;

public class AreasResource extends ListServerResource<AreaOld> {

    private BBApplication app;

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        app = (BBApplication)getApplication();
    }

    @Override
    public List<AreaOld> getEntity() {
        Filter filter = new Filter(getRequest());
        filter.add("owner", SecurityUtils.getSubject().getPrincipal().toString());
        Pagination pagination = new Pagination(getRequest(), getResponse(), 10);
        return null;//app.getRepository().findAll(Area.class, filter, pagination);
    }

}
