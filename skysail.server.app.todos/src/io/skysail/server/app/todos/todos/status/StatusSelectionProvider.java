package io.skysail.server.app.todos.todos.status;

import io.skysail.domain.html.SelectionProvider;
import io.skysail.server.app.todos.todos.Todo;
import io.skysail.server.restlet.resources.SkysailServerResource;

import java.util.*;
import java.util.stream.Collectors;

import org.restlet.resource.Resource;

public class StatusSelectionProvider implements SelectionProvider {

    public static StatusSelectionProvider getInstance() {
        return new StatusSelectionProvider();
    }

    private Resource resource;

    @Override
    public Map<String, String> getSelections() {
        Map<String, String> result = new HashMap<>();
        List<Status> statuses = Arrays.stream(Status.values()).collect(Collectors.toList());
        Status status;
        if (resource != null && resource instanceof SkysailServerResource) {
            Todo currentEntity = (Todo) ((SkysailServerResource<Todo>) resource).getCurrentEntity();
            status = currentEntity.getStatus();
            statuses = status.getNexts().stream().map(str -> Status.valueOf(str)).collect(Collectors.toList());
        }
        statuses.stream().forEach(v -> result.put(v.name(), v.name()));
        return result;
    }

    @Override
    public void setConfiguration(Object osgiServicesProvider) {
    }

    @Override
    public void setResource(Resource resource) {
        this.resource = resource;
    }

}
