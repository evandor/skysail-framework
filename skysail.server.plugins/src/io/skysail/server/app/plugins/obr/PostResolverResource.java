package io.skysail.server.app.plugins.obr;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.plugins.PluginApplication;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.felix.bundlerepository.Reason;
import org.apache.felix.bundlerepository.Resolver;
import org.restlet.data.Form;

import de.twenty11.skysail.server.core.restlet.PostEntityServerResource;

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

    public SkysailResponse<String> addEntity(ObrResource entity) {
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
        return new SkysailResponse<String>();
    }
}
