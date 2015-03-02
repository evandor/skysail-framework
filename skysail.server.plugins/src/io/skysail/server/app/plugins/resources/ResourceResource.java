package io.skysail.server.app.plugins.resources;

import io.skysail.server.app.plugins.PluginApplication;

import java.util.List;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.api.responses.Linkheader;
import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.EntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class ResourceResource extends EntityServerResource<Resource> {

    private String symbolicName;
    private String version;
    private PluginApplication app;

    public ResourceResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Details");
    }

    @Override
    protected void doInit() throws ResourceException {
        String identifier = getAttribute("id");
        if (identifier == null) {
            return;
        }
        String[] split = identifier.split(";");
        symbolicName = split[0];
        version = split[1];
        app = (PluginApplication) getApplication();
    }

    // @Override
    // public Resource getData() {
    // // TODO user input! sanitized???
    // String searchTerm = "(&(symbolicname=" + symbolicName + ")(version=" +
    // version + "))";
    // List<org.apache.felix.bundlerepository.Resource> discoverdResources =
    // app.discoverResources(searchTerm);
    // if (discoverdResources.size() != 1) {
    // throw new
    // IllegalStateException("expected exactly one bundle for this search");
    // }
    // return new Resource(discoverdResources.get(0),
    // Arrays.asList(app.getBundleContext().getBundles()));
    // }

    @Override
    public String getId() {
        return "7";
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

    @Override
    public List<Linkheader> getLinkheader() {
        return super.getLinkheader(ResourceResource.class);
    }

    @Override
    public Resource getEntity() {
        return null;
    }
}
