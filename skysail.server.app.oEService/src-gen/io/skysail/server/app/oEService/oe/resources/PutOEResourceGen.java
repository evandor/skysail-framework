package io.skysail.server.app.oEService.oe.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import java.util.Date;
import org.restlet.resource.ResourceException;
import io.skysail.server.app.oEService.*;
import io.skysail.server.app.oEService.oe.*;

/**
 * generated from putResource.stg
 */
public class PutOEResourceGen extends PutEntityServerResource<io.skysail.server.app.oEService.oe.OE> {


    protected String id;
    protected OEServiceApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (OEServiceApplication)getApplication();
    }

    @Override
    public void updateEntity(OE  entity) {
        io.skysail.server.app.oEService.oe.OE original = getEntity();
        copyProperties(original,entity);

        app.getRepository(io.skysail.server.app.oEService.oe.OE.class).update(original,app.getApplicationModel());
    }

    @Override
    public io.skysail.server.app.oEService.oe.OE getEntity() {
        return (io.skysail.server.app.oEService.oe.OE)app.getRepository(io.skysail.server.app.oEService.oe.OE.class).findOne(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(OEsResourceGen.class);
    }
}