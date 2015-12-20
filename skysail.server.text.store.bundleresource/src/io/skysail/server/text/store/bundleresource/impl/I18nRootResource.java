package io.skysail.server.text.store.bundleresource.impl;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.domain.Identifiable;
import io.skysail.server.restlet.resources.ListServerResource;

public class I18nRootResource extends ListServerResource<Identifiable> {

    @Override
    public List<Link> getLinks() {
        return super.getLinks(MessagesResource.class);
    }

    @Override
    public List<Identifiable> getEntity() {
        return null;
    }

}
