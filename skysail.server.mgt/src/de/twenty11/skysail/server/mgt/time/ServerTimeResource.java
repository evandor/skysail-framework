package de.twenty11.skysail.server.mgt.time;

import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.EntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import de.twenty11.skysail.server.mgt.ManagementApplication;

public class ServerTimeResource extends EntityServerResource<String> {

	private ManagementApplication app;

	public ServerTimeResource() {
	    app = (ManagementApplication)getApplication();
        addToContext(ResourceContextId.LINK_TITLE, "Server Time");
    }

	@Override
    public String getData() {
        return "hi";
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

}
