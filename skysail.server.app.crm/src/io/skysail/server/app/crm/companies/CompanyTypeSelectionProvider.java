package io.skysail.server.app.crm.companies;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.restlet.resource.Resource;

import de.twenty11.skysail.api.forms.SelectionProvider;

public class CompanyTypeSelectionProvider implements SelectionProvider {

    public static CompanyTypeSelectionProvider getInstance() {
        return new CompanyTypeSelectionProvider();
    }

    @Override
    public Map<String, String> getSelections() {
        Map<String, String> result = new HashMap<>();
        Arrays.stream(CompanyType.values()).forEach(v -> result.put(v.name(), v.name()));
        return result;
    }

    @Override
    public void setConfiguration(Object osgiServicesProvider) {
    }

    @Override
    public void setResource(Resource resource) {
    }

}
