package io.skysail.server.ext.apt.test.twoentities.jobs;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.PutEntityServerResource;

public class PutJobResource extends PutEntityServerResource<Job> {

	private String id;

	@Override
	protected void doInit() throws ResourceException {
		id = getAttribute("id");
	}

	@Override
	public Job getEntity() {
		return JobsRepository.getInstance().getById(id);
	}

	@Override
	public SkysailResponse<?> updateEntity(Job entity) {
		JobsRepository.getInstance().update(entity);
		return null;
	}

}
