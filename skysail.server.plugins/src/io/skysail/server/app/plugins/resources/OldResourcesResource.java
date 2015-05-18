package io.skysail.server.app.plugins.resources;

import io.skysail.server.app.plugins.PluginApplication;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.*;
import java.util.stream.Collectors;

import org.osgi.framework.Bundle;

public class OldResourcesResource extends ListServerResource<Resource> {

    public OldResourcesResource() {
        super(ResourceResource.class);
    }

    @Override
    public List<Resource> getEntity() {
        PluginApplication app = (PluginApplication) getApplication();
        List<org.apache.felix.bundlerepository.Resource> resources = app
                .discoverResources("(|(presentationname=*clip*)(symbolicname=*clip*))");
        List<Bundle> installedBundles = Arrays.asList(app.getBundleContext().getBundles());
        return resources.stream().map(r -> {
            return new Resource(r, installedBundles);
        }).sorted((r1, r2) -> {
            int first = r1.getSymbolicName().compareTo(r2.getSymbolicName());
            if (first != 0) {
                return first;
            }
            return -r1.getVersion().compareTo(r2.getVersion());
        }).collect(Collectors.toList());
    }

}
