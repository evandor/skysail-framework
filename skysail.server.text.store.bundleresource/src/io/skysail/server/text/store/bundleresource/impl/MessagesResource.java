package io.skysail.server.text.store.bundleresource.impl;

import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.*;

public class MessagesResource extends ListServerResource<BundleMessages> {

    private SkysailApplication app;

    public MessagesResource() {
        app = (SkysailApplication) getApplication();
    }

    @Override
    public List<BundleMessages> getEntity() {
        return Collections.emptyList();
    }

    
}
