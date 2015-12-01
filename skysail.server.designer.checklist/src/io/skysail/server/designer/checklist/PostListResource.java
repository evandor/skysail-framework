package io.skysail.server.designer.checklist;

import java.util.Date;

import io.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.resource.ResourceException;

public class PostResource extends PostEntityServerResource<> {

	private ChecklistApplication app;

    public PostResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new ");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (ChecklistApplication) getApplication();
    }

    @Override
    public  createEntityTemplate() {
        return new ();
    }

    @Override
    public SkysailResponse<?> addEntity( entity) {
        Subject subject = SecurityUtils.getSubject();
        String id = app.getRepository().add(entity).toString();
        entity.setId(id);
        return new SkysailResponse<String>();

    }

    @Override
    public String redirectTo() {
        return super.redirectTo(sResource.class);
    }
}