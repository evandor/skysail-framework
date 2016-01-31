package io.skysail.server.app.designer.fields.roles;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.restlet.resource.Resource;

import io.skysail.domain.html.SelectionProvider;
import io.skysail.server.app.designer.fields.FieldRole;

public class FieldRoleSelectionProvider implements SelectionProvider {

    public static FieldRoleSelectionProvider getInstance() {
        return new FieldRoleSelectionProvider();
    }

    private Resource resource;

    @Override
    public Map<String, String> getSelections() {
        Map<String, String> result = new HashMap<>();
        result.put("", "Select...");
        List<FieldRole> types = Arrays.stream(FieldRole.values()).collect(Collectors.toList());
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
