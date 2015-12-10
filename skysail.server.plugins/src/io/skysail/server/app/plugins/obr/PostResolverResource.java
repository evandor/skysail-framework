package io.skysail.server.app.plugins.obr;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.felix.bundlerepository.*;
import org.restlet.data.Form;

import io.skysail.server.app.plugins.PluginApplication;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class PostResolverResource extends PostEntityServerResource<ObrResource> {

    private PluginApplication app;

    public PostResolverResource() {
        app = (PluginApplication) getApplication();
    }

    @Override
    public ObrResource createEntityTemplate() {
        return null;
    }

    public ObrResource createEntity() {
        return new ObrResource();
    }

    public ObrResource getData(Form form) {
        ObrResource entity;
        entity = new ObrResource(form.getFirstValue("searchFor"));
        // note.setOwner(app.getCurrentUser().getId());
        return entity;
    }

    public void addEntity(ObrResource entity) {
        String filter = StringEscapeUtils.unescapeHtml(entity.getSearchFor());
        StringBuilder sb = new StringBuilder();
        sb.append("Discovering Resources for filter '").append(filter).append("'\n<br>");
        Resolver resolver = null;// app.discoverResources(filter);
        sb.append("found resources: ").append(resolver.getAddedResources()).append("'\n<br>");
        if (resolver.resolve()) {
            resolver.deploy(Resolver.START);
        } else {
            Reason[] reqs = resolver.getUnsatisfiedRequirements();
            for (int i = 0; i < reqs.length; i++) {
                sb.append("Unable to resolve: " + reqs[i]).append("\\n<br>");
            }
        }
    }
}
