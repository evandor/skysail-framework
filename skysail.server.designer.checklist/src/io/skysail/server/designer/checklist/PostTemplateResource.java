package io.skysail.server.designer.checklist;

import java.util.Date;

import io.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.resource.ResourceException;

public class PostTemplateResource extends PostEntityServerResource<io.skysail.server.designer.checklist.Template> {

	private ChecklistApplication app;

    public PostTemplateResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new ");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (ChecklistApplication) getApplication();
    }

    @Override
    public io.skysail.server.designer.checklist.Template createEntityTemplate() {
        return new Template();
    }

    @Override
    public void addEntity(io.skysail.server.designer.checklist.Template entity) {
        Subject subject = SecurityUtils.getSubject();
        String id = app.getRepository(io.skysail.server.designer.checklist.Template.class).save(entity).toString();
        entity.setId(id);

    }

    @Override
    public String redirectTo() {
        return super.redirectTo(TemplatesResource.class);
    }
}