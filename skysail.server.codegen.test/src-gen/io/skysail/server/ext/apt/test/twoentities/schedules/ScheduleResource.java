package io.skysail.server.ext.apt.test.twoentities.schedules;

import java.util.List;

import org.restlet.resource.ResourceException;


import de.twenty11.skysail.api.responses.Linkheader;
import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.EntityServerResource;

public class ScheduleResource extends EntityServerResource<Schedule> {

	private String id;

	@Override
	protected void doInit() throws ResourceException {
	    id = getAttribute("id");
	}

	@Override
	public Schedule getData() {
		return SchedulesRepository.getInstance().getById(id);
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	public SkysailResponse<?> eraseEntity() {
        SchedulesRepository.getInstance().delete(id);
        return new SkysailResponse<String>();
	}

    @Override
	public List<Linkheader> getLinkheader() {
        return super.getLinkheader(PutScheduleResource.class);
	}

}
