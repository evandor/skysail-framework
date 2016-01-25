package io.skysail.server.app.wiki;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import java.util.Date;
import org.restlet.resource.ResourceException;

public class PutSpaceResource extends PutEntityServerResource<io.skysail.server.app.wiki.Space> {


    protected String id;
    protected WikiApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (WikiApplication)getApplication();
    }

    @Override
    public void updateEntity(Space  entity) {
        io.skysail.server.app.wiki.Space original = getEntity();
        copyProperties(original,entity);

        app.getRepository(io.skysail.server.app.wiki.Space.class).update(original,app.getApplicationModel());
    }

    @Override
    public io.skysail.server.app.wiki.Space getEntity() {
        return (io.skysail.server.app.wiki.Space)app.getRepository(io.skysail.server.app.wiki.Space.class).findOne(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(SpacesResource.class);
    }
}