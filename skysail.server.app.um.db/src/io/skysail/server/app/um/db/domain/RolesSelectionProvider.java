package io.skysail.server.app.um.db.domain;

import io.skysail.api.forms.SelectionProvider;

import java.util.*;

import org.restlet.resource.Resource;

public class RolesSelectionProvider implements SelectionProvider {

    public static RolesSelectionProvider getInstance() {
        return new RolesSelectionProvider();
    }

    @Override
    public Map<String, String> getSelections() {
        Map<String,String> result = new HashMap<>();
        result.put("38:0", "Admin");
        result.put("38:1", "User");
        return result;
    }

    @Override
    public void setConfiguration(Object osgiServicesProvider) {
    }

    @Override
    public void setResource(Resource resource) {
    }

}
