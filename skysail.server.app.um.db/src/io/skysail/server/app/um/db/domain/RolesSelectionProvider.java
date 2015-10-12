package io.skysail.server.app.um.db.domain;

import io.skysail.api.forms.SelectionProvider;
import io.skysail.server.app.um.db.UmApplication;
import io.skysail.server.queryfilter.Filter;

import java.util.*;

import org.restlet.resource.Resource;

public class RolesSelectionProvider implements SelectionProvider {

    public static RolesSelectionProvider getInstance() {
        return new RolesSelectionProvider();
    }

    private Resource resource;

    @Override
    public Map<String, String> getSelections() {
        List<Role> allRoles = ((UmApplication)resource.getApplication()).getRoleRepo().find(new Filter());
        Map<String,String> result = new HashMap<>();
        allRoles.stream().forEach(r -> result.put(r.getId().toString(), r.getName()));
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
