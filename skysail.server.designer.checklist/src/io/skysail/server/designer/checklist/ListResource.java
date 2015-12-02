package io.skysail.server.designer.checklist;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class ListResource extends EntityServerResource<io.skysail.server.designer.checklist.List> {

    private String id;
    private ChecklistApplication app;

    public ListResource() {
        addToContext(ResourceContextId.LINK_TITLE, "details");
        addToContext(ResourceContextId.LINK_GLYPH, "search");
    }

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (ChecklistApplication) getApplication();
    }


    @Override
    public SkysailResponse<?> eraseEntity() {
        return new SkysailResponse<String>();
    }

    @Override
    public io.skysail.server.designer.checklist.List getEntity() {
        return (io.skysail.server.designer.checklist.List)app.getRepository().findOne(id);
    }

	@Override
    public List<Link> getLinks() {
        return super.getLinks(PutListResource.class);
    }

}