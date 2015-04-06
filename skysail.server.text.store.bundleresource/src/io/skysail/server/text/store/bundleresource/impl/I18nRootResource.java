package io.skysail.server.text.store.bundleresource.impl;

import io.skysail.api.links.Link;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

public class I18nRootResource extends ListServerResource<String> {

    @Override
    public List<Link> getLinkheader() {
        return super.getLinkheader(MessagesResource.class);
    }

    @Override
    public List<String> getEntity() {
        return null;
    }

}
