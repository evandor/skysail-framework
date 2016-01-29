package io.skysail.server.app.notes;

import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.api.links.Link;

import java.util.*;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class NotesResource extends ListServerResource<io.skysail.server.app.notes.Note> {

    private NotesApplication app;
    private NoteRepository repository;

    public NotesResource() {
        super(NoteResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list Notes");
    }

    public NotesResource(Class<? extends NoteResource> cls) {
        super(cls);
    }

    @Override
    protected void doInit() {
        app = (NotesApplication) getApplication();
        repository = (NoteRepository) app.getRepository(io.skysail.server.app.notes.Note.class);
    }

    @Override
    public List<io.skysail.server.app.notes.Note> getEntity() {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse(), repository.count(filter));
        return repository.find(filter, pagination);
    }

    public List<Link> getLinks() {
              return super.getLinks(PostNoteResource.class,NotesResource.class);
    }
}