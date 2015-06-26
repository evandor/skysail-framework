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
        super.doInit();
        id = getAttribute("id");
        app = (PropManApplication)getApplication();
    }

    @Override
    public SkysailResponse<?> updateEntity(Request  entity) {
<<<<<<< HEAD
        Request  original = getEntity();
=======
>>>>>>> 8f0daa6a1048a5118acd82fc33534b1649cf5887
        Request original = getEntity();

        app.getRepository().update(id, original);
        return new SkysailResponse<>();
    }

    @Override
    public Request getEntity() {
        return null;
    }
}