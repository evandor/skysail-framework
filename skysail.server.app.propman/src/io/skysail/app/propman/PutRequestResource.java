package io.skysail.app.propman;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import org.restlet.resource.ResourceException;

public class PutRequestResource extends PutEntityServerResource<Request> {

    private String id;
	private PropManApplication app;

	@Override
    protected void doInit() throws ResourceException {
        super.doInit();
        id = getAttribute("id");
        app = (PropManApplication)getApplication();
    }

    @Override
    public SkysailResponse<Request> updateEntity(Request entity) {
        Request original = getEntity();

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
