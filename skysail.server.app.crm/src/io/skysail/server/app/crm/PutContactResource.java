package io.skysail.server.app.cRM;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import java.util.Date;
import org.restlet.resource.ResourceException;

public class PutContactResource extends PutEntityServerResource<io.skysail.server.app.cRM.Contact> {


    private String id;
    private CRMApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (CRMApplication)getApplication();
    }

    @Override
    public SkysailResponse<io.skysail.server.app.cRM.Contact> updateEntity(Contact  entity) {
        io.skysail.server.app.cRM.Contact original = getEntity();
        copyProperties(original,entity);

        app.getRepository(io.skysail.server.app.cRM.Contact.class).update(id, original);
        return new SkysailResponse<>();
    }

    @Override
    public io.skysail.server.app.cRM.Contact getEntity() {
        return (io.skysail.server.app.cRM.Contact)app.getRepository(io.skysail.server.app.cRM.Contact.class).findOne(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(ContactsResource.class);
    }
}