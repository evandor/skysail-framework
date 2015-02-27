package io.skysail.server.app.contacts.domain.companies;

import java.util.List;

import org.restlet.resource.ResourceException;


import de.twenty11.skysail.api.responses.Linkheader;
import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.EntityServerResource;

public class CompanyResource extends EntityServerResource<Company> {

	private String id;

	@Override
	protected void doInit() throws ResourceException {
	    id = getAttribute("id");
	}

	@Override
	public Company getData() {
		return CompanysRepository.getInstance().getById(id);
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	public SkysailResponse<?> eraseEntity() {
        CompanysRepository.getInstance().delete(id);
        return new SkysailResponse<String>();
	}

    @Override
	public List<Linkheader> getLinkheader() {
        return super.getLinkheader(PutCompanyResource.class);
	}

}
