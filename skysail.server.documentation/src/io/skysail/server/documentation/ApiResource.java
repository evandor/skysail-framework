package io.skysail.server.documentation;

import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.RouteBuilder;

public class ApiResource extends ListServerResource<ResourceApi> {

    private SkysailApplication app;
    private String applicationName = "";

    /**
     * Describes the API for the associated application.
     * 
     * <p>
     * The API consists of paths, entities and resources. Paths are connected to
     * resources which manage entities.
     * </p>
     * 
     * <p>
     * A Get request to this resource analyzes all paths (and the associated
     * resources and entities) attached to the current application.
     * </p>
     * 
     */
    public ApiResource() {
        setDescription("Provides information about the RESTful API of this application");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (SkysailApplication) getApplication();
        applicationName = app.getName();// getRequest().getOriginalRef().getSegments().get(0);
    }

    @Override
    public List<ResourceApi> getEntity() {
        Map<String, RouteBuilder> routes = app.getRoutesMap();
        List<ResourceApi> result = new ArrayList<ResourceApi>();
        for (String path : routes.keySet()) {
            // TODO extend with redirectors and so on
            if (routes.get(path).getTargetClass() == null) {
                continue;
            }
            result.add(new ResourceApi(applicationName + path, routes.get(path)));
        }
        Collections.sort(result);
        return result;
    }

}
