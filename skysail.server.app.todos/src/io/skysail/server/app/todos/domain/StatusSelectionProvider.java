package io.skysail.server.app.todos.domain;

import io.skysail.api.forms.SelectionProvider;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.restlet.resource.Resource;

public class StatusSelectionProvider implements SelectionProvider {

    public static StatusSelectionProvider getInstance() {
        return new StatusSelectionProvider();
    }

    private Resource resource;

    @Override
    public Map<String, String> getSelections() {
        Map<String, String> result = new HashMap<>();
        
        
        
        Arrays.stream(Status.values()).forEach(v -> result.put(v.name(), v.name()));
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
