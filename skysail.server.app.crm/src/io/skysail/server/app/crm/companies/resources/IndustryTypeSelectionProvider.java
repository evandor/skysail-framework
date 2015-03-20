package io.skysail.server.app.crm.companies.resources;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.restlet.resource.Resource;

import io.skysail.api.forms.SelectionProvider;

public class IndustryTypeSelectionProvider implements SelectionProvider {

    public static IndustryTypeSelectionProvider getInstance() {
        return new IndustryTypeSelectionProvider();
    }

    @Override
    public Map<String, String> getSelections() {
        Map<String, String> result = new HashMap<>();
        Arrays.stream(IndustryType.values()).forEach(v -> result.put(v.name(), v.name()));
        return result;
    }

    @Override
    public void setConfiguration(Object osgiServicesProvider) {
    }

    @Override
    public void setResource(Resource resource) {
    }

}
