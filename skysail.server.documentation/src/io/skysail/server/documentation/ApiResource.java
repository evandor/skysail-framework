package io.skysail.server.documentation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.apidoc.API;
import de.twenty11.skysail.server.apidoc.ApplicationApi;
import de.twenty11.skysail.server.app.SkysailApplication;
import de.twenty11.skysail.server.core.restlet.ListServerResource;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;

public class ApiResource extends ListServerResource<ApplicationApi> {

    private SkysailApplication app;
    private String applicationName = "";

    /**
     * Describes the API for the associated application.
     */
    public ApiResource() {
        super(null);
        setDescription("Provides information about the RESTful API of this application");
    }

    @Override
    protected void doInit() throws ResourceException {
        // super.doInit();
        app = (SkysailApplication) getApplication();
        applicationName = app.getName();// getRequest().getOriginalRef().getSegments().get(0);
    }

    @Get("html|json|csv")
    @API(desc = "returns the list with paths and RESTful methods provided by this application")
    public List<ApplicationApi> getEntities() {
        return super.getEntities();
    }

    @Override
    public List<ApplicationApi> getData() {
        Map<String, RouteBuilder> routes = app.getRoutesMap();
        List<ApplicationApi> result = new ArrayList<ApplicationApi>();
        for (String path : routes.keySet()) {
            if (routes.get(path).getTargetClass() == null) { // TODO extend with
                                                             // redirectors and
                                                             // so on
                continue;
            }
            result.add(new ApplicationApi(applicationName + path, routes.get(path)));
        }
        Collections.sort(result);
        return result;
    }

}
