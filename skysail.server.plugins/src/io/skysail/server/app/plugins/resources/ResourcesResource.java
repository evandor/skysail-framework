package io.skysail.server.app.plugins.resources;

import java.util.List;

import de.twenty11.skysail.server.core.restlet.ListServerResource;

public class ResourcesResource extends ListServerResource<Resource> {

    public ResourcesResource() {
        super(ResourceResource.class);
    }

    @Override
    public List<Resource> getEntity() {
        return null;
    }

    // @Override
    // public List<Resource> getData() {
    // PluginApplication app = (PluginApplication) getApplication();
    // List<org.apache.felix.bundlerepository.Resource> resources = app
    // .discoverResources("(|(presentationname=*)(symbolicname=*))");
    // List<Bundle> installedBundles =
    // Arrays.asList(app.getBundleContext().getBundles());
    // return resources.stream().map(r -> {
    // return new Resource(r, installedBundles);
    // }).sorted((r1, r2) -> {
    // int first = r1.getSymbolicName().compareTo(r2.getSymbolicName());
    // if (first != 0) {
    // return first;
    // }
    // return -r1.getVersion().compareTo(r2.getVersion());
    // }).collect(Collectors.toList());
    // }
}
