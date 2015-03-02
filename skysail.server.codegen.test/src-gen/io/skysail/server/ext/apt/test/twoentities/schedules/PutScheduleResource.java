package io.skysail.server.ext.apt.test.twoentities.schedules;

import org.codehaus.jettison.json.JSONObject;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.PutEntityServerResource;

public class PutScheduleResource extends PutEntityServerResource<Schedule> {

    private String id;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
    }

    @Override
    public JSONObject getEntity() {
        return null;// SchedulesRepository.getInstance().getById(id);
    }

    @Override
    public SkysailResponse<?> updateEntity(Schedule entity) {
        SchedulesRepository.getInstance().update(entity);
        return null;
    }

}
