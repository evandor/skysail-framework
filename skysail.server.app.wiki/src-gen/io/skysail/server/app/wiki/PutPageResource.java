package io.skysail.server.app.wiki;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import java.util.Date;
import org.restlet.resource.ResourceException;

public class PutPageResource extends PutEntityServerResource<io.skysail.server.app.wiki.Page> {


    private String id;
    private WikiApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (WikiApplication)getApplication();
    }

    @Override
    public void updateEntity(Page  entity) {
        io.skysail.server.app.wiki.Page original = getEntity();
        copyProperties(original,entity);

        app.getRepository(io.skysail.server.app.wiki.Page.class).update(original,app.getApplicationModel());
    }

    @Override
    public io.skysail.server.app.wiki.Page getEntity() {
        return (io.skysail.server.app.wiki.Page)app.getRepository(io.skysail.server.app.wiki.Page.class).findOne(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(PagesResource.class);
    }
}