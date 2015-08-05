package de.twenty11.skysail.server.osgi.osgimonitor.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

import java.util.*;

import org.osgi.framework.*;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.osgi.OsgiMonitorViewerApplication;
import de.twenty11.skysail.server.osgi.osgimonitor.domain.ServiceDescriptor;

public class ServiceResource extends EntityServerResource<ServiceDescriptor> {

    private Long serviceId;
    private List<ServiceReference> services = new ArrayList<ServiceReference>();

    @Override
    protected void doInit() throws ResourceException {
        serviceId = Long.valueOf((String) getRequest().getAttributes().get("serviceId"));
        OsgiMonitorViewerApplication app = (OsgiMonitorViewerApplication) getApplication();
        BundleContext bundleContext = app.getBundleContext();
        if (bundleContext != null) {
            try {
                ServiceReference[] allServiceReferences = bundleContext.getAllServiceReferences(null, null);
                services = Arrays.asList(allServiceReferences);
            } catch (InvalidSyntaxException e) {
                throw new ResourceException(e);
            }
        }
    }

//    @Override
//    @Get("html|json")
//    public ServiceDescriptor getEntity() {
//        return getEntity("Details for service " + serviceId);
//    }

    @Override
    public ServiceDescriptor getEntity() {
        for (ServiceReference sr : services) {
            ServiceDescriptor serviceDescriptor = new ServiceDescriptor(sr, getReference());
            if (serviceDescriptor.getServiceId().equals(serviceId)) {
                return serviceDescriptor;
            }
        }
        return null;
    }

    public SkysailResponse<?> addEntity(ServiceDescriptor entity) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

}
