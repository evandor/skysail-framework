package de.twenty11.skysail.server.osgi.osgimonitor.resources;

import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.*;

import org.osgi.framework.*;
import org.restlet.representation.*;
import org.restlet.resource.*;

import de.twenty11.skysail.server.osgi.osgimonitor.domain.ServiceDescriptor;

/**
 * Restlet Resource for OSGi Services.
 *
 * The managed entity is of type {@link ServiceDescriptor}.
 *
 */
// @Presentation(preferred = PresentationStyle.LIST2)
public class ServicesResource extends ListServerResource<ServiceDescriptor> {

    private List<ServiceReference> services = Collections.emptyList();

    public ServicesResource() {
        super(null);
        // setName("osgimonitor bundles resource");
        // setDescription("The resource containing the list of bundles");
    }

    /**
     * initializes the services collection from the bundleContext.
     */
    @Override
    protected void doInit() throws ResourceException {
        try {
            BundleContext bundleContext = ((SkysailApplication) getApplication()).getBundleContext();
            ServiceReference[] allServiceReferences = bundleContext.getAllServiceReferences(null, null);
            services = Arrays.asList(allServiceReferences);
        } catch (InvalidSyntaxException e) {
            throw new ResourceException(e);
        }
    }

    @Override
    public List<ServiceDescriptor> getEntity() {
        List<ServiceDescriptor> result = new ArrayList<ServiceDescriptor>();
        for (ServiceReference sr : services) {
            ServiceDescriptor descriptor = new ServiceDescriptor(sr, getReference());
            result.add(descriptor);
        }
        Collections.sort(result);
        return result;
    }

    @Post
    public Representation install(String location) {
        String prefix = "prefix";
        if (!location.startsWith(prefix)) {
            return new StringRepresentation("location didn't start with '" + prefix + "'");
        }
        return new StringRepresentation("success");
    }

}
