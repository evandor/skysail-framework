package io.skysail.server.app.designer.application;

import java.util.List;

import de.twenty11.skysail.api.responses.Linkheader;
import de.twenty11.skysail.server.core.restlet.ListServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class ApplicationsResource extends ListServerResource<Application2> {

    public ApplicationsResource() {
        addToContext(ResourceContextId.LINK_TITLE, "list Applications");
    }

    @Override
    public List<Application2> getEntity() {
        return ApplicationsRepository.getInstance().getApplications();
    }

    @Override
    public List<Linkheader> getLinkheader() {
        return super.getLinkheader(PostApplicationResource.class);
    }

}
