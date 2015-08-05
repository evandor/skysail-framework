package de.twenty11.skysail.server.osgi.osgimonitor.resources;

import io.skysail.server.restlet.resources.ListServerResource;

import java.util.*;

import org.osgi.framework.*;
import org.osgi.framework.wiring.*;

import de.twenty11.skysail.server.osgi.OsgiMonitorViewerApplication;
import de.twenty11.skysail.server.osgi.osgimonitor.domain.Requirement;

//@Presentation(preferred = PresentationStyle.LIST2)
public class RequirementsResource extends ListServerResource<Requirement> {

    public RequirementsResource() {
        super(null);
    }
    @Override
    public List<Requirement> getEntity() {
        List<Requirement> result = new ArrayList<Requirement>();
        OsgiMonitorViewerApplication app = (OsgiMonitorViewerApplication) getApplication();

        BundleContext bundleContext = app.getBundleContext();
        Bundle[] bundles = bundleContext.getBundles();
        for (Bundle bundle : bundles) {
            addRequirements(result, bundle);
        }
        return result;
    }

    private void addRequirements(List<Requirement> result, Bundle bundle) {
        BundleWiring bw = bundle.adapt(BundleWiring.class);
        if (bw == null) {
            return;
        }
        List<BundleWire> providedWires = bw.getProvidedWires(BundleRevision.PACKAGE_NAMESPACE);

        for (BundleRequirement cap : bw.getRequirements(BundleRevision.PACKAGE_NAMESPACE)) {
            result.add(new Requirement(cap));
        }
    }

}
