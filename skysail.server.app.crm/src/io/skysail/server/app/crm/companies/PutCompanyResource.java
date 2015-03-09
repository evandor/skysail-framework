package io.skysail.server.app.crm.companies;

import io.skysail.server.app.crm.ContactsGen;

import java.util.Date;

import org.apache.shiro.SecurityUtils;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.PutEntityServerResource;

public class PutCompanyResource extends PutEntityServerResource<Company> {

    private String id;
    private ContactsGen app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (ContactsGen) getApplication();
    }

    @Override
    public Company getEntity() {
        return app.getRepository().getById(Company.class, id);
    }

    @Override
    public SkysailResponse<?> updateEntity(Company entity) {
        entity.setChanged(new Date());
        entity.setChangedBy(SecurityUtils.getSubject().getPrincipal().toString());
        app.getRepository().update(entity);
        return new SkysailResponse<String>();
    }

}