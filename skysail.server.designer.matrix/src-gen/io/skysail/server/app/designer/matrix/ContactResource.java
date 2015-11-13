package io.skysail.server.app.designer.matrix;

import java.util.List;
import javax.annotation.Generated;

import org.restlet.resource.ResourceException;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
public class ContactResource extends EntityServerResource<Contact> {

    private MatrixApplication app;
    private ContactRepo repository;

    protected void doInit() {
        super.doInit();
        app = (MatrixApplication)getApplication();
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
        return null;//super.getLinkheader();
    }
}
