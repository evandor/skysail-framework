package io.skysail.server.app.designer.matrix;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

import java.util.List;

import javax.annotation.Generated;

@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
public class ContactResource extends EntityServerResource<Contact> {

    private io.skysail.server.app.designer.matrix.MatrixApplication app;
    private ContactRepo repository;

    protected void doInit() {
        super.doInit();
        app = (io.skysail.server.app.designer.matrix.MatrixApplication)getApplication();
        repository = (ContactRepo) app.getRepository(Contact.class);
    }

    public Contact getEntity() {
        return repository.findOne(getAttribute("id"));
    }

    public List<Link> getLinks() {
        return super.getLinks(PutContactResource.class);
    }

    public SkysailResponse<Contact> eraseEntity() {
        repository.delete(getAttribute("id"));
        return new SkysailResponse<>();
    }

    @Override
    public String redirectTo() {
        return null;//super.getLinkheader(PutContactResource.class);
    }
}
