package io.skysail.server.text.store.bundleresource.impl;

import java.util.List;

import de.twenty11.skysail.api.responses.Linkheader;
import de.twenty11.skysail.server.core.restlet.ListServerResource;

public class I18nRootResource extends ListServerResource<String> {

    // @Override
    // public List<String> getData() {
    // return Collections.emptyList();
    // }

    @Override
    public List<Linkheader> getLinkheader() {
        return super.getLinkheader(MessagesResource.class);
    }

    @Override
    public List<String> getEntity() {
        // TODO Auto-generated method stub
        return null;
    }

}
