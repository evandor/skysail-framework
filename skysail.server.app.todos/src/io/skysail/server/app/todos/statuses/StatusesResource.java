package io.skysail.server.app.todos.statuses;

import io.skysail.server.app.todos.todos.status.Status;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.*;


public class StatusesResource extends ListServerResource<Status> {

    @Override
    public List<Status> getEntity() {
//        return Arrays.stream(Status.values()).map(Status::valueOf).collect(Collectors.toList());
        return Arrays.asList(Status.values());
    }

}
