package io.skysail.server.ext.apt.test.twoentities.schedules;

import java.util.List;
import java.util.function.Consumer;

import org.restlet.resource.ResourceException;

import io.skysail.server.ext.apt.test.twoentities.schedules.*;


import de.twenty11.skysail.api.responses.Linkheader;
import de.twenty11.skysail.server.core.restlet.ListServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class SchedulesResource extends ListServerResource<Schedule> {

    private String id;

    public SchedulesResource() {
        super(ScheduleResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "List of Schedules");
    }

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
    }

    @Override
    public List<Schedule> getData() {
        return SchedulesRepository.getInstance().getSchedules();
    }

    @Override
    public List<Linkheader> getLinkheader() {
        return super.getLinkheader(PostScheduleResource.class);
    }

    @Override
    public Consumer<? super Linkheader> getPathSubstitutions() {
        return l -> { l.substitute("id", id); };
    }
}
