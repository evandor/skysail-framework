package io.skysail.server.app.designer.relations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.restlet.resource.Resource;

import io.skysail.domain.core.EntityRelationType;
import io.skysail.domain.html.SelectionProvider;

public class RelationTypeSelectionProvider implements SelectionProvider {

    public static RelationTypeSelectionProvider getInstance() {
        return new RelationTypeSelectionProvider();
    }

    private Resource resource;

    @Override
    public Map<String, String> getSelections() {
        Map<String, String> result = new HashMap<>();
        List<EntityRelationType> statuses = Arrays.stream(EntityRelationType.values()).collect(Collectors.toList());
//        Status status;
//        if (resource != null && resource instanceof SkysailServerResource) {
//            Todo currentEntity = (Todo) ((SkysailServerResource<Todo>) resource).getCurrentEntity();
//            status = currentEntity.getStatus();
//            statuses = status.getNexts().stream().map(str -> Status.valueOf(str)).collect(Collectors.toList());
//        }
        statuses.stream().forEach(v -> result.put(v.name(), v.name()));
        return result;    }

    @Override
    public void setConfiguration(Object osgiServicesProvider) {
    }

    @Override
    public void setResource(Resource resource) {
        this.resource = resource;
    }

}
