package de.twenty11.skysail.server.osgi.osgimonitor.resources;

import io.skysail.api.links.Link;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.*;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

/**
 * Restlet Root Resource for dbViewer application.
 *
 */
public class OsgiMonitorRootResource extends ListServerResource<String> {

    public OsgiMonitorRootResource() {
        super(null);
        addToContext(ResourceContextId.LINK_TITLE, "OSGi Monitor");
    }


    @Override
    public List<String> getEntity() {
        return Collections.emptyList();
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(BundlesResource.class, BundlesGraphResource.class);
    }


}
