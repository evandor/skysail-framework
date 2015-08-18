package de.twenty11.skysail.server.osgi.osgimonitor.resources;

import io.skysail.server.restlet.resources.ListServerResource;

import java.util.*;

import org.osgi.framework.*;
import org.restlet.representation.*;
import org.restlet.resource.*;

import de.twenty11.skysail.server.osgi.OsgiMonitorViewerApplication;
import de.twenty11.skysail.server.osgi.osgimonitor.domain.BundleDescriptor;

/**
 * Restlet Resource class for OSGi Bundles.
 *
 */
public class BundlesGraphResource extends ListServerResource<BundleDescriptor> {

    private List<Bundle> bundles;
    private OsgiMonitorViewerApplication app;

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        app = (OsgiMonitorViewerApplication) getApplication();
        BundleContext bundleContext = app.getBundleContext();
        if (bundleContext == null) {
            bundles = Collections.emptyList();
        } else {
            bundles = Arrays.asList(bundleContext.getBundles());
        }
    }

    @Post
    public Representation install(String location) {
        String prefix = "prefix";
        if (!location.startsWith(prefix)) {
            return new StringRepresentation("location didn't start with '" + prefix + "'");
        }
        return new StringRepresentation("success");
    }

    @Override
    public List<BundleDescriptor> getEntity() {
        List<BundleDescriptor> result = new ArrayList<BundleDescriptor>();
        for (Bundle bundle : bundles) {
            BundleDescriptor bundleDescriptor = new BundleDescriptor(bundle);
            // if (filterMatches(bundleDescriptor)) {
            result.add(bundleDescriptor);
            // }
        }
        return result;
    }

}
