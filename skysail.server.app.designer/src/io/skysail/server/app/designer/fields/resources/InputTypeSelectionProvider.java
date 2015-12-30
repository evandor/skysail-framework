package io.skysail.server.app.designer.fields.resources;

import java.util.*;
import java.util.stream.Collectors;

import org.restlet.resource.Resource;

import io.skysail.domain.html.*;

public class InputTypeSelectionProvider implements SelectionProvider {

    public static InputTypeSelectionProvider getInstance() {
        return new InputTypeSelectionProvider();
    }

    private Resource resource;

    @Override
    public Map<String, String> getSelections() {
        Map<String, String> result = new HashMap<>();
        List<InputType> types = Arrays.stream(InputType.values()).collect(Collectors.toList());
//        InputType status;
//        if (resource != null && resource instanceof SkysailServerResource) {
//            Todo currentEntity = ((SkysailServerResource<Todo>) resource).getCurrentEntity();
//            status = currentEntity.getStatus();
//            statuses = status.getNexts().stream().map(str -> Status.valueOf(str)).collect(Collectors.toList());
//        }
        types.stream().forEach(v -> result.put(v.name(), v.name()));
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
