package io.skysail.server.text.store.bundleresource.impl;

import io.skysail.server.restlet.resources.ListServerResource;

import java.util.Collections;
import java.util.List;

public class MessagesResource extends ListServerResource<BundleMessages> {

    private I18nApplication app;

    public MessagesResource() {
        app = (I18nApplication) getApplication();
    }

    @Override
    public List<BundleMessages> getEntity() {
        return Collections.emptyList();
    }

    
}
