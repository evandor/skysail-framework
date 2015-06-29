package io.skysail.app.propman;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import java.util.Date;
import org.restlet.resource.ResourceException;

public class PutRequestResource extends PutEntityServerResource<Request> {


    private String id;
    private PropManApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (PropManApplication)getApplication();
    }

    @Override
    public SkysailResponse<?> updateEntity(Request  entity) {
        Request original = getEntity();
        copyProperties(original,entity);

        app.getRepository().update(id, original);
        return new SkysailResponse<>();
    }

    @Override
    public Request getEntity() {
        return app.getRepository().getById(Request.class, id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(RequestsResource.class);
    }
}