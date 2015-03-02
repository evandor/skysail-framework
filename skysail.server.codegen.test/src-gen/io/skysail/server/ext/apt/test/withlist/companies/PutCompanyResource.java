package io.skysail.server.ext.apt.test.withlist.companies;

import org.codehaus.jettison.json.JSONObject;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.PutEntityServerResource;

public class PutCompanyResource extends PutEntityServerResource<Company> {

    private String id;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
    }

    @Override
    public JSONObject getEntity() {
        return null;// CompanysRepository.getInstance().getById(id);
    }

    @Override
    public SkysailResponse<?> updateEntity(Company entity) {
        CompanysRepository.getInstance().update(entity);
        return null;
    }

}
