package io.skysail.server.app.todos.statuses;

import io.skysail.api.domain.Identifiable;
import io.skysail.server.app.todos.todos.status.Status;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

import org.restlet.resource.ResourceException;

public class NextStatusesResource extends ListServerResource<Identifiable> {

    private String statusname;

    @Override
    protected void doInit() throws ResourceException {
        statusname = getAttribute("statusname");
    }

    @Override
    public List<String> getEntity() {
        return Status.valueOf(statusname).getNexts();
    }

}
