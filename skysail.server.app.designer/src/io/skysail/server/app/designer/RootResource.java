package io.skysail.server.app.designer;

import io.skysail.server.app.designer.application.ApplicationsResource;

import java.util.List;

import de.twenty11.skysail.api.responses.Linkheader;
import de.twenty11.skysail.server.core.restlet.ListServerResource;

public class RootResource extends ListServerResource<String> {

    @Override
    public List<Linkheader> getLinkheader() {
        return super.getLinkheader(ApplicationsResource.class);
    }

}