package io.skysail.api.forms;

import java.util.Collections;
import java.util.Map;

import org.restlet.resource.Resource;

public class IgnoreSelectionProvider implements SelectionProvider {

    @Override
    public Map<String, String> getSelections() {
        return Collections.emptyMap();
    }

    @Override
    public void setConfiguration(Object osgiServicesProvider) {
    }

    @Override
    public void setResource(Resource resource) {
    }

}
