package io.skysail.server.designer.checklist;

import java.util.Date;

import io.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.resource.ResourceException;

public class PostListResource extends PostEntityServerResource<io.skysail.server.designer.checklist.List> {

	private ChecklistApplication app;

    public PostListResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new ");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (ChecklistApplication) getApplication();
    }

    @Override
    public io.skysail.server.designer.checklist.List createEntityTemplate() {
        return new List();
    }

    @Override
    public SkysailResponse<io.skysail.server.designer.checklist.List> addEntity(io.skysail.server.designer.checklist.List entity) {
        Subject subject = SecurityUtils.getSubject();
        //String id = app.getRepository().add(entity).toString();
        //entity.setId(id);
        return new SkysailResponse<>();

    }

    @Override
    public String redirectTo() {
        return super.redirectTo(ListsResource.class);
    }
}