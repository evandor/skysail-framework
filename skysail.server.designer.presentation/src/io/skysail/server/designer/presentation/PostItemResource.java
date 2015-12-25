package io.skysail.server.designer.presentation;

import java.util.Date;

import io.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.resource.ResourceException;

public class PostItemResource extends PostEntityServerResource<io.skysail.server.designer.presentation.Item> {

	private PresentationApplication app;

    public PostItemResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new ");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (PresentationApplication) getApplication();
    }

    @Override
    public io.skysail.server.designer.presentation.Item createEntityTemplate() {
        return new Item();
    }

    @Override
    public void addEntity(io.skysail.server.designer.presentation.Item entity) {
        Subject subject = SecurityUtils.getSubject();
        String id = app.getRepository(io.skysail.server.designer.presentation.Item.class).save(entity, app.getApplicationModel()).toString();
        entity.setId(id);

    }

    @Override
    public String redirectTo() {
        return super.redirectTo(ItemsResource.class);
    }
}