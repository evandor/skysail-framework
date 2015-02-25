package io.skysail.server.documentation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.app.SkysailApplication;
import de.twenty11.skysail.server.core.restlet.ListServerResource;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;

public class EntitiesResource extends ListServerResource<EntityDescriptor> {

    private SkysailApplication app;
    private String applicationName = "";
    private String name;

    /**
     * Describes the entities of the associated application.
     */
    public EntitiesResource() {
        super(null);
        app = (SkysailApplication) getApplication();
        setDescription("Provides information about the RESTful Entities of this application");
    }

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        applicationName = getRequest().getOriginalRef().getSegments().get(0);
        if (getRequest().getAttributes().get("name") != null) {
            name = (String) getRequest().getAttributes().get("name");
        }
    }

    @Override
    public List<EntityDescriptor> getData() {
        Set<EntityDescriptor> entities = new HashSet<EntityDescriptor>();
        Map<String, RouteBuilder> routes = app.getRoutesMap();
        for (String path : routes.keySet()) {
            ResourceApi applicationApi = new ResourceApi(applicationName + path, routes.get(path));
            if (applicationApi.getEntity() == null) {
                continue;
            }
            if (name != null && applicationApi.getEntity().getName().equals(name)) {
                entities.add(applicationApi.getEntity());
            } else if (name == null) {
                entities.add(applicationApi.getEntity());
            }
        }

        ArrayList<EntityDescriptor> result = new ArrayList<EntityDescriptor>();
        result.addAll(entities);
        return result;
    }

}
