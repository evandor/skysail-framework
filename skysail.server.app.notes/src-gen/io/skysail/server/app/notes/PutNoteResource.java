package io.skysail.server.app.notes;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import java.util.Date;
import org.restlet.resource.ResourceException;

public class PutNoteResource extends PutEntityServerResource<io.skysail.server.app.notes.Note> {


    private String id;
    private NotesApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (NotesApplication)getApplication();
    }

    @Override
    public void updateEntity(Note  entity) {
        io.skysail.server.app.notes.Note original = getEntity();
        copyProperties(original,entity);

        app.getRepository(io.skysail.server.app.notes.Note.class).update(original,app.getApplicationModel());
    }

    @Override
    public io.skysail.server.app.notes.Note getEntity() {
        return (io.skysail.server.app.notes.Note)app.getRepository(io.skysail.server.app.notes.Note.class).findOne(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(NotesResource.class);
    }
}