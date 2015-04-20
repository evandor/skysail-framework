package io.skysail.server.app.todos.todos.resources;

import io.skysail.api.forms.SelectionProvider;

import java.util.HashMap;
import java.util.Map;

import org.restlet.resource.Resource;

public class ListSelectionProvider implements SelectionProvider {

    public static ListSelectionProvider getInstance() {
        return new ListSelectionProvider();
    }

    private Resource resource;

    @Override
    public Map<String, String> getSelections() {
        Map<String, String> result = new HashMap<>();
        result.put("hi", "hi");
//        List<Status> statuses = Arrays.stream(Status.values()).collect(Collectors.toList());
//        Status status;
//        if (resource != null && resource instanceof SkysailServerResource) {
//            Todo currentEntity = ((SkysailServerResource<Todo>) resource).getCurrentEntity();
//            status = currentEntity.getStatus();
//            statuses = status.getNexts().stream().map(str -> Status.valueOf(str)).collect(Collectors.toList());
//        }
//        statuses.stream().forEach(v -> result.put(v.name(), v.name()));
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
