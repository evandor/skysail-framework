package de.twenty11.skysail.server.core.test;

import io.skysail.api.forms.SelectionProvider;

import java.util.HashMap;
import java.util.Map;

import org.restlet.resource.Resource;

public class DummySelectionProvider implements SelectionProvider {

    public static DummySelectionProvider getInstance() {
        return new DummySelectionProvider();
    }

    @Override
    public Map<String, String> getSelections() {
        Map<String, String> result = new HashMap<>();
        result.put("A", "1");
        return result;
    }

    @Override
    public void setConfiguration(Object osgiServicesProvider) {
    }

    @Override
    public void setResource(Resource resource) {
    }

}