package io.skysail.server.app.notes;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class NoteResource extends EntityServerResource<io.skysail.server.app.notes.Note> {

    private String id;
    private NotesApplication app;
    private NoteRepository repository;

    public NoteResource() {
        addToContext(ResourceContextId.LINK_TITLE, "details");
        addToContext(ResourceContextId.LINK_GLYPH, "search");
    }

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (NotesApplication) getApplication();
        repository = (NoteRepository) app.getRepository(io.skysail.server.app.notes.Note.class);
    }


    @Override
    public SkysailResponse<?> eraseEntity() {
        repository.delete(id);
        return new SkysailResponse<>();
    }

    @Override
    public io.skysail.server.app.notes.Note getEntity() {
        return (io.skysail.server.app.notes.Note)app.getRepository().findOne(id);
    }

	@Override
    public List<Link> getLinks() {
        return super.getLinks(PutNoteResource.class);
    }

}