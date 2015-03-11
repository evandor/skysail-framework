package io.skysail.server.app.crm.companies.resources;

import io.skysail.server.app.crm.CrmApplication;
import io.skysail.server.app.crm.companies.Company;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.restlet.resource.Resource;

import de.twenty11.skysail.api.forms.SelectionProvider;

public class CompanySelectionProvider implements SelectionProvider {

    public static CompanySelectionProvider getInstance() {
        return new CompanySelectionProvider();
    }

    private static CrmApplication app;

    @Override
    public Map<String, String> getSelections() {
        Map<String, String> result = new HashMap<>();
        result.put("", "");

        List<Company> findAll = app.getRepository().findAll(Company.class);
        findAll.stream().forEach(c -> {
            // result.put(c.getId().toString(), c.getName());
            });
        return result;
    }

    @Override
    public void setConfiguration(Object osgiServicesProvider) {
    }

    @Override
    public void setResource(Resource resource) {
        app = (CrmApplication) resource.getApplication();
    }

}
