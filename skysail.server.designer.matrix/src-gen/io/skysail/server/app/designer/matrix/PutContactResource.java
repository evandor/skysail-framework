package io.skysail.server.app.designer.matrix;

import javax.annotation.Generated;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
public class PutContactResource extends PutEntityServerResource<Contact> {

    private MatrixApplication app;
    private ContactRepo repository;

	protected void doInit() {
        super.doInit();
        app = (MatrixApplication) getApplication();
        repository = (ContactRepo) app.getRepository(Contact.class);
    }

    public Contact getEntity() {
        return repository.findOne(getAttribute("id"));
    }

    public SkysailResponse<Contact> updateEntity(Contact entity) {
        repository.update(getAttribute("id"), entity);
        return new SkysailResponse<>();
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(ContactsResource.class);
    }
}
