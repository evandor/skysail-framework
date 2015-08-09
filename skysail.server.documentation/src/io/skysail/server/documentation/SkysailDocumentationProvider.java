package io.skysail.server.documentation;

import io.skysail.api.documentation.DocumentationProvider;

import java.util.*;

import org.restlet.resource.ServerResource;

import aQute.bnd.annotation.component.Component;

@Component(immediate = true)
public class SkysailDocumentationProvider implements DocumentationProvider {

    private static final String APPLICATION_API_PATH = "/docs/api";
    private static final String APPLICATION_ENTITIES_PATH = "/docs/entities";

    @Override
    public Map<String, Class<? extends ServerResource>> getResourceMap() {
        Map<String, Class<? extends ServerResource>> result = new HashMap<>();
        result.put(APPLICATION_API_PATH, ApiResource.class);
        result.put(APPLICATION_ENTITIES_PATH, EntitiesResource.class);
        result.put(APPLICATION_ENTITIES_PATH + "/{id}", EntityResource.class);
        // result.put(APPLICATION_LINKS_PATH, LinksResource.class));
        return result;
    }
}
