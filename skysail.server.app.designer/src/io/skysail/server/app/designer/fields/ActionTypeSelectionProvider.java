package io.skysail.server.app.designer.fields;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.restlet.resource.Resource;

import io.skysail.domain.html.SelectionProvider;

public class ActionTypeSelectionProvider implements SelectionProvider {

    public static ActionTypeSelectionProvider getInstance() {
        return new ActionTypeSelectionProvider();
    }

    @Override
    public Map<String, String> getSelections() {
        Map<String, String> result = new HashMap<>();
        List<ActionType> types = Arrays.stream(ActionType.values()).collect(Collectors.toList());
        types.stream().forEach(v -> result.put(v.name(), v.name()));
        return result;
    }

    @Override
    public void setConfiguration(Object osgiServicesProvider) {
    }

    @Override
    public void setResource(Resource resource) {
    }

}
