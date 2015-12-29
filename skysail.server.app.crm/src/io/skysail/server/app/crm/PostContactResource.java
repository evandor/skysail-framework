package io.skysail.server.app.cRM;

import java.util.Date;

import io.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.resource.ResourceException;

public class PostContactResource extends PostEntityServerResource<io.skysail.server.app.cRM.Contact> {

	private CRMApplication app;

    public PostContactResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new ");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (CRMApplication) getApplication();
    }

    @Override
    public io.skysail.server.app.cRM.Contact createEntityTemplate() {
        return new Contact();
    }

    @Override
    public void addEntity(io.skysail.server.app.cRM.Contact entity) {
        Subject subject = SecurityUtils.getSubject();
        String id = app.getRepository(io.skysail.server.app.cRM.Contact.class).save(entity, app.getApplicationModel()).toString();
        entity.setId(id);

    }

    @Override
    public String redirectTo() {
        return super.redirectTo(ContactsResource.class);
    }
}