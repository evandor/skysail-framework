package io.skysail.server.text.store.bundleresource.impl;

import java.util.Collections;
import java.util.List;

import de.twenty11.skysail.api.responses.Linkheader;
import de.twenty11.skysail.server.core.restlet.ListServerResource;

public class I18nRootResource extends ListServerResource<String> {

    @Override
    public List<String> getData() {
        return Collections.emptyList();
    }

    @Override
    public List<Linkheader> getLinkheader() {
        return super.getLinkheader(MessagesResource.class);
    }

}
