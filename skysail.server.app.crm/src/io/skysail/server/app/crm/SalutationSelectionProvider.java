package io.skysail.server.app.crm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.restlet.resource.Resource;

import de.twenty11.skysail.api.forms.SelectionProvider;

public class SalutationSelectionProvider implements SelectionProvider {

    @Override
    public Map<String, String> getSelections() {
        Map<String, String> result = new HashMap<>();
        Arrays.stream(Salutation.values()).forEach(v -> {
            result.put(v.name(), v.name());
        });
        return result;
    }

    @Override
    public void setConfiguration(Object osgiServicesProvider) {
    }

    @Override
    public void setResource(Resource resource) {
    }

}
