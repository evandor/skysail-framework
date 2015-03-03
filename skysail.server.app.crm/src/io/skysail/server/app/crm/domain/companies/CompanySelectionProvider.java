package io.skysail.server.app.crm.domain.companies;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.restlet.resource.Resource;

import de.twenty11.skysail.api.forms.SelectionProvider;

public class CompanySelectionProvider implements SelectionProvider {

    public static CompanySelectionProvider getInstance() {
        return new CompanySelectionProvider();
    }

    @Override
    public Map<String, String> getSelections() {
        Map<String, String> result = new HashMap<>();
        result.put("", "");
        List<Map<String, Object>> companies = CompaniesRepository.getInstance().getCompanys();
        companies.stream().forEach(company -> {
            result.put(company.get("@rid").toString(), (String) company.get("name"));
        });
        if (result.isEmpty()) {
            result.put("default", "default");
        }
        return result;
    }

    @Override
    public void setConfiguration(Object osgiServicesProvider) {
    }

    @Override
    public void setResource(Resource resource) {
    }

}
