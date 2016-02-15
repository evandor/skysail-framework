package io.skysail.server.app.notes;

import java.util.Date;

import io.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.resource.ResourceException;

public class PostNoteResource extends PostEntityServerResource<io.skysail.server.app.notes.Note> {

	protected NotesApplication app;

    public PostNoteResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new ");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (NotesApplication) getApplication();
    }

    @Override
    public io.skysail.server.app.notes.Note createEntityTemplate() {
        return new Note();
    }

    @Override
    public void addEntity(io.skysail.server.app.notes.Note entity) {
        Subject subject = SecurityUtils.getSubject();
        entity.setUuid(java.util.UUID.randomUUID().toString());
        String id = app.getRepository(io.skysail.server.app.notes.Note.class).save(entity, app.getApplicationModel()).toString();
        entity.setId(id);

    }

    @Override
    public String redirectTo() {
        return super.redirectTo(NotesResource.class);
    }
}