package de.twenty11.skysail.server.resources;

import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.app.SkysailRootApplication;
import de.twenty11.skysail.server.core.restlet.EntityServerResource;

public class VersionResource extends EntityServerResource<Map<String, String>> {

    private SkysailRootApplication app;

    public VersionResource() {
        app = (SkysailRootApplication) getApplication();
    }

    @Override
    public Map<String, String> getEntity() {
        Map<String, String> result = new HashMap<String, String>();
        result.put("application", getMainVersion());
        return result;
    }

    private String getMainVersion() {
        BundleContext bundleContext = app.getBundleContext();
        if (bundleContext == null) {
            return "---";
        }
        String versionFrom = ""; //app.getConfigForKey(Constants.SKYSAIL_VERSION_FROM);
        for (Bundle bundle : bundleContext.getBundles()) {
            if (bundle.getSymbolicName().equals(versionFrom)) {
                String versionString = bundle.getVersion().toString();
                String[] parts = versionString.split("\\.");
                if (parts.length != 4) {
                    return versionString;
                }
                return parts[0] + "." + parts[1] + "." + parts[2] + "&nbsp;(" + parts[3].substring(6, 8) + "."
                        + parts[3].substring(4, 6) + "." + parts[3].substring(0, 4) + "&nbsp;"
                        + parts[3].substring(8, 10) + ":" + parts[3].substring(10, 12) + ")";
            }
        }
        return "'versionFrom' missing from configuration";
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
