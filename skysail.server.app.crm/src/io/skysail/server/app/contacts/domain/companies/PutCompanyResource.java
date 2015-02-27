package io.skysail.server.app.contacts.domain.companies;

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
	public Company getEntity() {
		return CompanysRepository.getInstance().getById(id);
	}

	@Override
	public SkysailResponse<?> updateEntity(Company entity) {
		CompanysRepository.getInstance().update(entity);
		return null;
	}

}
