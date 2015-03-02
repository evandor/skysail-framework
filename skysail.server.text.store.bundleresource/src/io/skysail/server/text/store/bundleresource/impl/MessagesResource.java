package io.skysail.server.text.store.bundleresource.impl;

import de.twenty11.skysail.server.core.restlet.ListServerResource;

public class MessagesResource extends ListServerResource<BundleMessages> {

    private I18nApplication app;

    public MessagesResource() {
        app = (I18nApplication) getApplication();
    }

    // @Override
    // public List<BundleMessages> getData() {
    // return app.getBundleMessages(new Locale("en"));
    // }
}
