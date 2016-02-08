package io.skysail.server.app.designer.fields.provider;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.restlet.resource.Resource;

import io.skysail.domain.html.SelectionProvider;
import io.skysail.server.forms.Visibility;

public class VisibilitySelectionProvider implements SelectionProvider {

    public static VisibilitySelectionProvider getInstance() {
        return new VisibilitySelectionProvider();
    }

    private Resource resource;

    @Override
    public Map<String, String> getSelections() {
        Map<String, String> result = new HashMap<>();
        List<Visibility> types = Arrays.stream(Visibility.values()).collect(Collectors.toList());
        types.stream().forEach(v -> result.put(v.name(), v.name().toLowerCase()));
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
