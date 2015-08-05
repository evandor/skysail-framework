package de.twenty11.skysail.server.mgt.apps;

import io.skysail.server.restlet.resources.ListServerResource;

import java.util.*;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import de.twenty11.skysail.server.mgt.ManagementApplication;

public class ApplicationsResource extends ListServerResource<ApplicationDescriptor> {

    private ManagementApplication app;

    public ApplicationsResource() {
        super(null);
        app = (ManagementApplication) getApplication();
        addToContext(ResourceContextId.LINK_TITLE, "Applications");
    }

    @Override
    public List<ApplicationDescriptor> getData() {
        List<ApplicationDescriptor> result = new ArrayList<ApplicationDescriptor>();
//        OsgiServicesProvider osgiServicesProvider = app.getOsgiServicesProvider();
//        if (osgiServicesProvider == null) {
//            return result;
//        }
//        for (Application app : osgiServicesProvider.getApplications()) {
//            result.add(new ApplicationDescriptor(app));
//        }
        return result;
    }

}
